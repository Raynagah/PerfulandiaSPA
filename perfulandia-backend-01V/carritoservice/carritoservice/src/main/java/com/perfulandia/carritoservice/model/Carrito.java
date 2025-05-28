package com.perfulandia.carritoservice.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long usuarioId;
    
    @ElementCollection
    @CollectionTable(name = "carrito_productos", joinColumns = @JoinColumn(name = "carrito_id"))
    @Column(name = "producto_id")
    private List<Long> productosIds = new ArrayList<>();
    
    @Transient
    @JsonIgnore
    private List<Producto> productos = new ArrayList<>();
    
    @Transient
    private double total;
    
    public double getTotal() {
        this.total = productos.stream()
                   .mapToDouble(Producto::getPrecio)
                   .sum();
        return this.total;
    }
}