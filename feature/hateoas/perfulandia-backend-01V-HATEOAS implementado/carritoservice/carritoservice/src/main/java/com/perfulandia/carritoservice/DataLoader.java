package com.perfulandia.carritoservice;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.repository.CarritoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CarritoRepository repository;

    public DataLoader(CarritoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        repository.deleteAll();
        if (repository.count() == 0) {
            // Crear algunos carritos de ejemplo
            Carrito carrito1 = new Carrito();
            carrito1.setUsuarioId(1L);

            Carrito carrito2 = new Carrito();
            carrito2.setUsuarioId(2L);

            repository.saveAll(List.of(carrito1, carrito2));
            System.out.println("Carritos de prueba creados");
        }
    }
}