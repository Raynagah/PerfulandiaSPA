package com.perfulandia.carritoservice.controller;

import com.perfulandia.carritoservice.assembler.CarritoAssembler;
import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.service.CarritoService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService service;
    private final CarritoAssembler assembler;

    public CarritoController(CarritoService service, CarritoAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<EntityModel<Carrito>> getCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = service.obtenerCarritoPorUsuario(usuarioId);
        return ResponseEntity.ok(assembler.toModel(carrito));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Carrito>>> getAllCarritos() {
        List<EntityModel<Carrito>> carritos = service.obtenerTodosLosCarritos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(carritos,
                linkTo(methodOn(CarritoController.class).getAllCarritos()).withSelfRel()));
    }

    //Corregir metodos en los otros microservicios :(
    @PostMapping("/{usuarioId}/productos")
    public ResponseEntity<EntityModel<Carrito>> agregarProducto(
            @PathVariable Long usuarioId,
            @RequestBody Producto producto) {
        Carrito carrito = service.agregarProducto(usuarioId, producto);
        return ResponseEntity.ok(assembler.toModel(carrito));
    }

    //Corregir metodos en los otros microservicios :(
    @DeleteMapping("/{usuarioId}/productos/{productoId}")
    public ResponseEntity<EntityModel<Carrito>> eliminarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        Carrito carrito = service.eliminarProducto(usuarioId, productoId);
        return ResponseEntity.ok(assembler.toModel(carrito));
    }

    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<?> vaciarCarrito(@PathVariable Long usuarioId) {
        service.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}