package com.perfulandia.carritoservice.repository;

import com.perfulandia.carritoservice.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

// Esta interfaz se encarga de guardar/leer carritos en la base de datos
// Extiende JpaRepository que ya tiene métodos como save(), findAll(), etc.
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Busca un carrito usando el ID del usuario
    Carrito findByUsuarioId(Long usuarioId);
}
