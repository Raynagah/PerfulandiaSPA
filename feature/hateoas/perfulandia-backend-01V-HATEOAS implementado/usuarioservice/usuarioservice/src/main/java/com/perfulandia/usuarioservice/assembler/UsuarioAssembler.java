package com.perfulandia.usuarioservice.assembler;

import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.controller.UsuarioController;

import org.springframework.hateoas.EntityModel; //Representando un recurso con enlaces
import org.springframework.hateoas.server.RepresentationModelAssembler;//Convierte un modelo en HATEOAS :o
import org.springframework.stereotype.Component;//Marca como un componente de Spring
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component //Declara esta clase como uno, va a ser una clase que transforma un Usuario en un EntityModel
public class UsuarioAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override //Convirtiendo el modelo en un EM con enlaces
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,

                //Enlace al recurso actual
                linkTo(methodOn(UsuarioController.class).buscarPorId(usuario.getId())).withSelfRel(),

                //Enlace a toda la lista de usuarios
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("Obtención de usuarios"),

                linkTo(methodOn(UsuarioController.class).agregarUsuario(null)).withRel("Creación de usuarios"),

                linkTo(methodOn(UsuarioController.class).eliminarUsuario(usuario.getId())).withRel("Eliminar usuario"));
    }
}
