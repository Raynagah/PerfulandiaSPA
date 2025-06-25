package com.perfulandia.productservice.assembler;

import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.controller.ProductoController;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override //Convirtiendo el modelo en un EM con enlaces
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,

                //Enlace al recurso actual
                linkTo(methodOn(ProductoController.class).buscarPorId(producto.getId())).withSelfRel(),

                //Enlace a toda la lista de productos
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("Listar Productos"),

                //Enlace a agregar producto
                linkTo(methodOn(ProductoController.class).agregarProducto(producto)).withRel("Agregar Producto"),

                //Enlace a eliminar producto
                linkTo(methodOn(ProductoController.class).eliminarProducto(producto.getId())).withRel("Eliminar Producto"));
    }

}
