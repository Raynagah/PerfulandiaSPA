package com.perfulandia.carritoservice.controller;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.model.Usuario;
import com.perfulandia.carritoservice.service.CarritoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    private final CarritoService servicio;
    private final RestTemplate restTemplate;

    @Value("${productos.service.url}")
    private String productosServiceUrl;

    @Value("${usuarios.service.url}")
    private String usuariosServiceUrl;

    public CarritoController(CarritoService servicio, RestTemplate restTemplate) {
        this.servicio = servicio;
        this.restTemplate = restTemplate;
    }

    // Clase DTO interna para la respuesta
    public static class CarritoResponse {
        private Long id;
        private Long usuarioId;
        private List<Long> productosIds;
        private double total;

        // Constructor vacío necesario para Jackson
        public CarritoResponse() {}

        // Constructor con parámetros
        public CarritoResponse(Long id, Long usuarioId, List<Long> productosIds, double total) {
            this.id = id;
            this.usuarioId = usuarioId;
            this.productosIds = productosIds;
            this.total = total;
        }

        // Getters y Setters (necesarios para la serialización JSON)
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }

        public List<Long> getProductosIds() {
            return productosIds;
        }

        public void setProductosIds(List<Long> productosIds) {
            this.productosIds = productosIds;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoResponse> obtenerCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = servicio.obtenerCarritoPorUsuario(usuarioId);
        CarritoResponse response = new CarritoResponse(
            carrito.getId(),
            carrito.getUsuarioId(),
            carrito.getProductosIds(),
            carrito.getTotal()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{usuarioId}/agregar/{productoId}")
    public ResponseEntity<?> agregarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        
        try {
            Producto producto = restTemplate.getForObject(
                productosServiceUrl + "/api/productos/" + productoId,
                Producto.class);
            
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("Producto no encontrado");
            }
            
            Carrito carrito = servicio.agregarProducto(usuarioId, producto);
            CarritoResponse response = new CarritoResponse(
                carrito.getId(),
                carrito.getUsuarioId(),
                carrito.getProductosIds(),
                carrito.getTotal()
            );
            return ResponseEntity.ok(response);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                   .body("Error al comunicarse con el servicio de productos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{usuarioId}/eliminar/{productoId}")
    public ResponseEntity<CarritoResponse> eliminarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        Carrito carrito = servicio.eliminarProducto(usuarioId, productoId);
        CarritoResponse response = new CarritoResponse(
            carrito.getId(),
            carrito.getUsuarioId(),
            carrito.getProductosIds(),
            carrito.getTotal()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{usuarioId}/resumen")
public ResponseEntity<String> obtenerResumen(@PathVariable Long usuarioId) {
    try {
        // Obtener carrito con productos ya cargados
        Carrito carrito = servicio.obtenerCarritoPorUsuario(usuarioId);
        
        // Obtener datos del usuario
        Usuario usuario = restTemplate.getForObject(
            usuariosServiceUrl + "/api/usuarios/" + usuarioId,
            Usuario.class);
        
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body("Usuario no encontrado");
        }
        
        // Construir el resumen
        StringBuilder resumen = new StringBuilder();
        resumen.append(String.format(
            "Carrito de %s\nTotal productos: %d\n\nDetalle:\n",
            usuario.getNombre(),
            carrito.getProductos().size()
        ));
        
        // Listar productos con nombre y precio
        for (Producto producto : carrito.getProductos()) {
            resumen.append(String.format(
                "- %s: $%.2f\n",
                producto.getNombre(),
                producto.getPrecio()
            ));
        }
        
        resumen.append(String.format("\nTotal a pagar: $%.2f", carrito.getTotal()));
        
        return ResponseEntity.ok(resumen.toString());
        
    } catch (RestClientException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
               .body("Error al comunicarse con servicios externos: " + e.getMessage());
    }
    }
}