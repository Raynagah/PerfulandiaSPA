package com.perfulandia.carritoservice.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producto {
    private Long id;
    private String nombre;
    private double precio;
    private int stock;
}