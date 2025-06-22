package com.perfulandia.carritoservice.model;

import lombok.*;

// Información básica de un usuario
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private long id;       // Identificador único
    private String nombre; // Ej: "María García"
    private String correo; // Ej: "maria@ejemplo.com"
    private String rol;    // Ej: "cliente" o "administrador"
}
