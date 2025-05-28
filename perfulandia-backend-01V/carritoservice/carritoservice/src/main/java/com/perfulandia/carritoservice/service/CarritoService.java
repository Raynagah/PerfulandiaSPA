package com.perfulandia.carritoservice.service;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {
    private final CarritoRepository repository;
    private final RestTemplate restTemplate;

    @Value("${productos.service.url}")
    private String productosServiceUrl;

    public CarritoService(CarritoRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Carrito obtenerCarritoPorUsuario(Long usuarioId) {
        Carrito carrito = repository.findByUsuarioId(usuarioId);
        if (carrito == null) {
            carrito = Carrito.builder()
                    .usuarioId(usuarioId)
                    .productosIds(new ArrayList<>())
                    .productos(new ArrayList<>())
                    .build();
            repository.save(carrito);
        }
        
        // Cargar productos actuales desde el servicio de productos
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
                // Log error pero continuar con otros productos
                System.err.println("Error obteniendo producto ID: " + productoId);
            }
        }
        carrito.setProductos(productosActuales);
        carrito.getTotal(); // Recalcula el total
        
        return carrito;
    }

    public Carrito agregarProducto(Long usuarioId, Producto producto) {
        if (producto == null || producto.getId() == null) {
            throw new IllegalArgumentException("Producto no válido");
        }
        
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);
        if (!carrito.getProductosIds().contains(producto.getId())) {
            carrito.getProductosIds().add(producto.getId());
            carrito.getProductos().add(producto);
            repository.save(carrito);
        }
        return carrito;
    }

    public Carrito eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);
        carrito.getProductosIds().remove(productoId);
        carrito.getProductos().removeIf(p -> p.getId().equals(productoId));
        return repository.save(carrito);
    }

    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);
        carrito.getProductosIds().clear();
        carrito.getProductos().clear();
        repository.save(carrito);
    }
}