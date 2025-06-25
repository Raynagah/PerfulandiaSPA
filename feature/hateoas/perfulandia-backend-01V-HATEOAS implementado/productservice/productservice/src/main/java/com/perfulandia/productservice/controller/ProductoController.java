package com.perfulandia.productservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.perfulandia.productservice.assembler.ProductoAssembler;
import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.service.ProductoService;

import java.util.List;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;
    private final ProductoAssembler assembler;

    public ProductoController(ProductoService service, ProductoAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    //Get
    @GetMapping
    public CollectionModel<EntityModel<Producto>> listarProductos() {

        List<EntityModel<Producto>> productos = service.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel()); //enlace del listado a si mismo :o


    }

    @GetMapping("/{id}")
    public EntityModel<Producto> buscarPorId(@PathVariable Long id) {

        Producto producto = service.bucarPorId(id);//Buscando por ID
        return assembler.toModel(producto);//Devolviendo el producto en EntityModel con enlaces de HATEOAS

    }

    @PostMapping
    public EntityModel<Producto> agregarProducto(@RequestBody Producto nuevoProducto) {
        // Guardar el nuevo usuario en la base de datos
        Producto ProductoGuardado = service.guardar(nuevoProducto);

        // Convertir a EntityModel con enlaces HATEOAS usando el assembler
        EntityModel<Producto> resource = assembler.toModel(ProductoGuardado);

        // Retornar respuesta HTTP 201 (Created) con el recurso y sus enlaces
        return assembler.toModel(ProductoGuardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> eliminarProducto(@PathVariable Long id) {
        // Verificar si el usuario existe
        Producto producto = service.bucarPorId(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        // Eliminar el usuario
        service.eliminar(id);

        // Obtener la lista actualizada de usuarios
        List<EntityModel<Producto>> usuarios = service.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        // Retornar la lista actualizada con enlaces
        CollectionModel<EntityModel<Producto>> recursos = CollectionModel.of(usuarios,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());

        return ResponseEntity.ok(recursos);
    }


}
