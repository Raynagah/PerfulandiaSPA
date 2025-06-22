package com.perfulandia.carritoservice.service;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    // Obtiene el carrito de un usuario (lo crea si no existe)
    public Carrito obtenerCarritoPorUsuario(Long usuarioId) {
        // Busca en la base de datos
        Carrito carrito = repository.findByUsuarioId(usuarioId);
        
        // Si no existe, crea uno nuevo
        if (carrito == null) {
            carrito = Carrito.builder()
                    .usuarioId(usuarioId)
                    .productosIds(new ArrayList<>())
                    .productos(new ArrayList<>())
                    .build();
            repository.save(carrito);
        }
        
        // Para cada ID de producto, obtiene sus detalles del servicio de productos
        List<Producto> productosActuales = new ArrayList<>();
        for (Long productoId : carrito.getProductosIds()) {
            try {
                Producto producto = restTemplate.getForObject(
                    productosServiceUrl + "/api/productos/" + productoId, 
                    Producto.class);
                if (producto != null) {
                    productosActuales.add(producto);
                }
            } catch (Exception e) {
                System.err.println("Error al obtener producto ID: " + productoId);
            }
        }
        
        // Actualiza la lista de productos y calcula el total
        carrito.setProductos(productosActuales);
        carrito.getTotal();
        
        return carrito;
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
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);
        carrito.getProductosIds().remove(productoId);
        carrito.getProductos().removeIf(p -> p.getId().equals(productoId));
        return repository.save(carrito);
    }

    // Vacía completamente el carrito
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);
        carrito.getProductosIds().clear();
        carrito.getProductos().clear();
        repository.save(carrito);
    }
}
