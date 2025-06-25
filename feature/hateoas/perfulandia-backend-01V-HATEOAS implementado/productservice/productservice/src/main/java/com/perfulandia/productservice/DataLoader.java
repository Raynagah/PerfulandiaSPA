package com.perfulandia.productservice;

import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository repo;

    public DataLoader(ProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        repo.deleteAll();

        if (repo.count()== 0){
            repo.saveAll(List.of(
                    new Producto("Polo Blue",124990,20),
                    new Producto("Polo Black",124990,10),
                    new Producto("Polo Red",124990,30)
            ));
            System.out.println("Perfumes de prueba creados");
        }
    }

}
