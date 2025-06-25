package com.perfulandia.usuarioservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder //Crear objetos de manera flexible = Constructor Flex
public class Usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String correo;
    private String rol; // ADMIN, GERENTE, Usuario

    // Constructor sin ID
    public Usuario(String nombre, String correo, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }
}
