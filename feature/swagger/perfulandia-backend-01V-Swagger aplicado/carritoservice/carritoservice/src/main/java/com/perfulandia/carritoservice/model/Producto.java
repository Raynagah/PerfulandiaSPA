package com.perfulandia.carritoservice.model;

import lombok.*;

// Representa un producto que puede añadirse al carrito
@Data  // Crea getters, setters, toString, etc.
@AllArgsConstructor
@NoArgsConstructor
@Builder  // Permite crear: Producto.builder().nombre("Zapatos").precio(50).build()
public class Producto {
    private Long id;       // Número único del producto
    private String nombre; // Ej: "Camiseta azul"
    private double precio; // Ej: 19.99
    private int stock;     // Cantidad disponible
}
