package com.perfulandia.usuarioservice.controller;

//Este import nos permite hacer una llamada al metodo del controlador
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//Importaciones adicionales
//Permite agregar links de HATEOAS basados en metodos
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.perfulandia.usuarioservice.assembler.UsuarioAssembler;
import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.service.UsuarioService;

import java.util.List;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
//Cross origin nos permite hacer consultas de ttodo tipo
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioAssembler assembler;

    public UsuarioController(UsuarioService service, UsuarioAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    //Get
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
        //1- Obteniendo todos los usuarios, transformamos en un EntityModel con enlaces de Assembler

        List<EntityModel<Usuario>> usuarios = service.listar().stream()
                .map(assembler::toModel)//transformando cada usuario en un modelo de entidad <Usuario>
                .collect(Collectors.toList());
        return CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel()); //enlace del listado a si mismo :o


    }

    @GetMapping("/{id}")
    public EntityModel<Usuario> buscarPorId(@PathVariable Long id) {

        Usuario usuario = service.buscar(id);//Buscando por ID
        return assembler.toModel(usuario);//Devolviendo el usuario en EntityModel con enlaces de HATEOAS

    }

    @PostMapping
    public EntityModel<Usuario> agregarUsuario(@RequestBody Usuario nuevoUsuario) {
        // Guardar el nuevo usuario en la base de datos
        Usuario usuarioGuardado = service.guardar(nuevoUsuario);

        // Convertir a EntityModel con enlaces HATEOAS usando el assembler
        EntityModel<Usuario> resource = assembler.toModel(usuarioGuardado);

        // Retornar respuesta HTTP 201 (Created) con el recurso y sus enlaces
        return assembler.toModel(usuarioGuardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> eliminarUsuario(@PathVariable Long id) {
        // Verificar si el usuario existe
        Usuario usuario = service.buscar(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        // Eliminar el usuario
        service.eliminar(id);

        // Obtener la lista actualizada de usuarios
        List<EntityModel<Usuario>> usuarios = service.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        // Retornar la lista actualizada con enlaces
        CollectionModel<EntityModel<Usuario>> recursos = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel());

        return ResponseEntity.ok(recursos);
    }
}
