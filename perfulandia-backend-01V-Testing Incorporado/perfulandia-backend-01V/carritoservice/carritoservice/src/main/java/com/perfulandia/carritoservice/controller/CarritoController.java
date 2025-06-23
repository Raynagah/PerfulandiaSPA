package com.perfulandia.carritoservice.controller;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.model.Usuario;
import com.perfulandia.carritoservice.service.CarritoService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j  // Para registrar mensajes en el sistema
@RestController  // Indica que esta clase maneja peticiones web
@RequiredArgsConstructor  // Crea constructor con campos finales
@RequestMapping("/api/carrito")  // Todas las rutas empiezan con esto
public class CarritoController {
    private final CarritoService servicio;  // Lógica del carrito
    private final RestTemplate restTemplate;  // Para llamar a otros servicios

    @Setter
    @Value("${productos.service.url}")  // URL del servicio de productos
    private String productosServiceUrl;

    @Setter
    @Value("${usuarios.service.url}")  // URL del servicio de usuarios
    private String usuariosServiceUrl;

    // Clase auxiliar para devolver respuestas consistentes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarritoResponse {
        private Long id;
        private Long usuarioId;
        private List<Long> productosIds;
        private double total;
    }

    // Obtiene el carrito de un usuario (GET /api/carrito/123)
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

    // Añade un producto al carrito (POST /api/carrito/123/agregar/456)
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
                    .body("Error al comunicarse con el servicio de productos");
        }
    }

    @DeleteMapping("/{usuarioId}/eliminar/{productoId}")
    public ResponseEntity<CarritoResponse> eliminarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        Carrito carrito = servicio.eliminarProducto(usuarioId, productoId);
        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CarritoResponse(
                carrito.getId(),
                carrito.getUsuarioId(),
                carrito.getProductosIds(),
                carrito.getTotal()
        ));
    }

    // Genera un resumen bonito del carrito (GET /api/carrito/123/resumen)
    @GetMapping("/{usuarioId}/resumen")
    public ResponseEntity<String> obtenerResumen(@PathVariable Long usuarioId) {
        try {
            Carrito carrito = servicio.obtenerCarritoPorUsuario(usuarioId);
            // Obtiene datos del usuario
            Usuario usuario = restTemplate.getForObject(
                usuariosServiceUrl + "/api/usuarios/" + usuarioId,
                Usuario.class);
            
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("Usuario no encontrado");
            }
            
            // Construye un texto con toda la información
            StringBuilder resumen = new StringBuilder();
            resumen.append("Carrito de " + usuario.getNombre() + "\n");
            resumen.append("Total productos: " + carrito.getProductos().size() + "\n\n");
            resumen.append("Detalle:\n");
            
            for (Producto p : carrito.getProductos()) {
                resumen.append("- " + p.getNombre() + ": $" + p.getPrecio() + "\n");
            }
            
            resumen.append("\nTotal a pagar: $" + carrito.getTotal());
            
            return ResponseEntity.ok(resumen.toString());
            
        } catch (RestClientException e) {
            log.error("Error al comunicarse con servicios externos", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                   .body("Error al obtener información");
        }
    }

    //Metodo para vacíar un carrito (nuevo)
    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<?> vaciarCarrito(@PathVariable Long usuarioId) {
        servicio.vaciarCarrito(usuarioId);
        return ResponseEntity.ok().build();
    }
}
