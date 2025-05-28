package com.perfulandia.carritoservice.controller;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.model.Usuario;
import com.perfulandia.carritoservice.service.CarritoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carrito")
public class CarritoController {
    private final CarritoService servicio;
    private final RestTemplate restTemplate;

    @Value("${productos.service.url}")
    private String productosServiceUrl;

    @Value("${usuarios.service.url}")
    private String usuariosServiceUrl;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarritoResponse {
        private Long id;
        private Long usuarioId;
        private List<Long> productosIds;
        private double total;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoResponse> obtenerCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = servicio.obtenerCarritoPorUsuario(usuarioId);
        return ResponseEntity.ok(new CarritoResponse(
            carrito.getId(),
            carrito.getUsuarioId(),
            carrito.getProductosIds(),
            carrito.getTotal()
        ));
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
            return ResponseEntity.ok(new CarritoResponse(
                carrito.getId(),
                carrito.getUsuarioId(),
                carrito.getProductosIds(),
                carrito.getTotal()
            ));
        } catch (RestClientException e) {
            log.error("Error al comunicarse con el servicio de productos", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                   .body("Error al comunicarse con el servicio de productos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{usuarioId}/eliminar/{productoId}")
    public ResponseEntity<CarritoResponse> eliminarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        Carrito carrito = servicio.eliminarProducto(usuarioId, productoId);
        return ResponseEntity.ok(new CarritoResponse(
            carrito.getId(),
            carrito.getUsuarioId(),
            carrito.getProductosIds(),
            carrito.getTotal()
        ));
    }

    @GetMapping("/{usuarioId}/resumen")
    public ResponseEntity<String> obtenerResumen(@PathVariable Long usuarioId) {
        try {
            Carrito carrito = servicio.obtenerCarritoPorUsuario(usuarioId);
            Usuario usuario = restTemplate.getForObject(
                usuariosServiceUrl + "/api/usuarios/" + usuarioId,
                Usuario.class);
            
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("Usuario no encontrado");
            }
            
            StringBuilder resumen = new StringBuilder();
            resumen.append(String.format(
                "Carrito de %s\nTotal productos: %d\n\nDetalle:\n",
                usuario.getNombre(),
                carrito.getProductos().size()
            ));
            
            carrito.getProductos().forEach(producto -> 
    resumen.append(String.format(
        "- %s: $%.2f\n",
        producto.getNombre(),
        producto.getPrecio()
                ))
            );
            
            resumen.append(String.format("\nTotal a pagar: $%.2f", carrito.getTotal()));
            
            return ResponseEntity.ok(resumen.toString());
            
        } catch (RestClientException e) {
            log.error("Error al comunicarse con servicios externos", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                   .body("Error al comunicarse con servicios externos: " + e.getMessage());
        }
    }
}