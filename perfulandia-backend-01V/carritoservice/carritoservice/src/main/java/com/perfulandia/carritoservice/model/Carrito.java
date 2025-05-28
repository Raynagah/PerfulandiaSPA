package com.perfulandia.carritoservice.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

// Esta clase define cómo es un carrito de compras
@Entity  // Indica que se guarda en la base de datos
@Data  // Crea automáticamente getters, setters, etc.
@AllArgsConstructor  // Crea constructor con todos los campos
@NoArgsConstructor   // Crea constructor vacío
@Builder  // Permite crear carritos fácilmente: Carrito.builder().usuarioId(1).build()
public class Carrito {
    @Id  // Este campo es el identificador único
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // El ID se genera automáticamente
    private Long id;
    
    private Long usuarioId;  // Guarda el ID del dueño del carrito
    
    // Lista de IDs de productos (se guarda en una tabla aparte en la base de datos)
    @ElementCollection
    @CollectionTable(name = "carrito_productos", joinColumns = @JoinColumn(name = "carrito_id"))
    @Column(name = "producto_id")
    private List<Long> productosIds = new ArrayList<>();
    
    // Lista de objetos Producto completos (NO se guarda en la base de datos)
    @Transient
    @JsonIgnore  // No se incluye en respuestas JSON
    private List<Producto> productos = new ArrayList<>();
    
    // Precio total (NO se guarda, se calcula cada vez)
    @Transient
    private double total;
    
    // Suma los precios de todos los productos
    public double getTotal() {
        this.total = 0;
        for (Producto p : productos) {
            this.total += p.getPrecio();
        }
        return this.total;
    }
}
