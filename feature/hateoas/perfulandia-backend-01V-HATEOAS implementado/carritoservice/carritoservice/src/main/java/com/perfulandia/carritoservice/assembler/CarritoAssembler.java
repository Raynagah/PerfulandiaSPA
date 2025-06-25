package com.perfulandia.carritoservice.assembler;

import com.perfulandia.carritoservice.controller.CarritoController;
import com.perfulandia.carritoservice.model.Carrito;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CarritoAssembler implements RepresentationModelAssembler<Carrito, EntityModel<Carrito>> {

    @Override
    public EntityModel<Carrito> toModel(Carrito carrito) {
        return EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).getCarrito(carrito.getUsuarioId())).withSelfRel(),
                linkTo(methodOn(CarritoController.class).getAllCarritos()).withRel("Listar todos los carritos"),
                linkTo(methodOn(CarritoController.class).agregarProducto(carrito.getUsuarioId(), null)).withRel("Agregar producto al carrito"),
                linkTo(methodOn(CarritoController.class).eliminarProducto(carrito.getUsuarioId(), 0L)).withRel("Eliminar producto del carrito").expand(),
                linkTo(methodOn(CarritoController.class).vaciarCarrito(carrito.getUsuarioId())).withRel("Limpiar carrito")
        );
    }

    @Override
    public CollectionModel<EntityModel<Carrito>> toCollectionModel(Iterable<? extends Carrito> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(CarritoController.class).getAllCarritos()).withSelfRel());
    }
}