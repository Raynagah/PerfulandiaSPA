package com.perfulandia.carritoservice.service;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service  // Indica que esta clase contiene la lógica principal del carrito
public class CarritoService {
    private final CarritoRepository repository;  // Para hablar con la base de datos
    private final RestTemplate restTemplate;    // Para comunicarse con otros servicios

    @Value("${productos.service.url}")  // URL del servicio de productos (se configura en application.properties)
    private String productosServiceUrl;

    // Constructor (Spring inyecta automáticamente las dependencias)
    public CarritoService(CarritoRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Carrito obtenerCarritoPorUsuario(Long usuarioId) {
        // 1. Obtener o crear el carrito
        Carrito carrito = repository.findByUsuarioId(usuarioId);

        if (carrito == null) {
            carrito = Carrito.builder()
                    .usuarioId(usuarioId)
                    .productosIds(new ArrayList<>())
                    .productos(new ArrayList<>())
                    .build();
            carrito = repository.save(carrito);
        }

        // 2. Sincronizar productosIds con productos
        sincronizarProductosDelCarrito(carrito);

        return carrito;
    }

    private void sincronizarProductosDelCarrito(Carrito carrito) {
        // 1. Verificar si ya están sincronizados
        if (carrito.getProductosIds().size() == carrito.getProductos().size() &&
                !hayProductosFaltantes(carrito)) {
            return;
        }

        // 2. Obtener todos los productos en una sola llamada (más eficiente)
        List<Producto> productosActualizados = obtenerProductosDesdeServicio(carrito.getProductosIds());

        // 3. Mantener solo los productos cuyos IDs están en la lista
        List<Producto> productosFiltrados = productosActualizados.stream()
                .filter(p -> carrito.getProductosIds().contains(p.getId()))
                .toList();

        // 4. Actualizar la lista
        carrito.setProductos(new ArrayList<>(productosFiltrados));
    }

    private List<Producto> obtenerProductosDesdeServicio(List<Long> productosIds) {
        if (productosIds.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Versión mejorada: obtiene todos los productos en una sola llamada
            ParameterizedTypeReference<List<Producto>> typeRef = new ParameterizedTypeReference<>() {};
            ResponseEntity<List<Producto>> response = restTemplate.exchange(
                    productosServiceUrl + "/api/productos/batch?ids={ids}",
                    HttpMethod.GET,
                    null,
                    typeRef,
                    productosIds.stream().map(String::valueOf).collect(Collectors.joining(","))
            );

            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private boolean hayProductosFaltantes(Carrito carrito) {
        Set<Long> idsEnLista = carrito.getProductos().stream()
                .map(Producto::getId)
                .collect(Collectors.toSet());

        return !idsEnLista.containsAll(carrito.getProductosIds());
    }

    // Añade un producto al carrito
    public Carrito agregarProducto(Long usuarioId, Producto producto) {
        if (producto == null || producto.getId() == null) {
            throw new IllegalArgumentException("Producto no válido");
        }
        
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);
        // Evita duplicados
        if (!carrito.getProductosIds().contains(producto.getId())) {
            carrito.getProductosIds().add(producto.getId());
            carrito.getProductos().add(producto);
            repository.save(carrito);
        }
        return carrito;
    }

    // Elimina un producto del carrito
    public Carrito eliminarProducto(Long usuarioId, Long productoId) {
        // 1. Obtener el carrito
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);

        // 2. Verificar si el producto existe
        boolean idExiste = carrito.getProductosIds().remove(productoId);
        boolean productoExiste = carrito.getProductos().removeIf(p -> p.getId().equals(productoId));

        // 3. Solo guardar si hubo cambios
        if (idExiste || productoExiste) {
            return repository.save(carrito);
        }

        // 4. Devolver el carrito sin cambios si no se encontró el producto
        return carrito;
    }

    // Vacía completamente el carrito
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);
        carrito.getProductosIds().clear();
        carrito.getProductos().clear();
        repository.save(carrito);
    }
}
