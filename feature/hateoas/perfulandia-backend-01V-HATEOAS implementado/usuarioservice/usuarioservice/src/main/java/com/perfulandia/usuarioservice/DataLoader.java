package com.perfulandia.usuarioservice;

import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;//Nos permite ejecutar codigo automaticamente al iniciar la aplicación
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository repo;

    public DataLoader(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public void run(String... args) {

        repo.deleteAll(); // Elimina todos los registros existentes
        // Verificar si ya existen datos
        if (repo.count() == 0) {
            // Usar saveAll para mejor performance
            repo.saveAll(List.of(
                    new Usuario("Rodrigo Vargas", "ro.vargas@duocuc.cl", "ADMIN"),
                    new Usuario("Martin Baza", "ma.baza@duocuc.cl", "ADMIN"),
                    new Usuario("Nicolás Bello", "ni.bello@duocuc.cl", "ADMIN")
            ));
            System.out.println("Usuarios de prueba creados");
        }
    }




}
