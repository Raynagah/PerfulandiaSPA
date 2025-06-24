package com.perfulandia.usuarioservice.controller;

import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;
import com.perfulandia.usuarioservice.service.UsuarioService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios de Perfulandia") //Anotación para Swagger
public class UsuarioController {

    private final UsuarioService service;
    //Constructor para poder consumir la interfaz
    public UsuarioController(UsuarioService service){
        this.service=service;
    }

    @GetMapping
    @Operation(summary = "Listar los usuarios existentes") //Descripción del  endpoint, aplica para cada metodo que tengamos :p
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuarios listados satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no listados, vuelva a intentarlo",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Usuarios no listados"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor, imposible realizar operación",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Error procesando la solicitud"
                    ))
            )
    })
    public List<Usuario> listar(){
        return service.listar();
    }

    @PostMapping
    @Operation(summary = "Crear y guardar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario creado y guardado satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no creado ni guardado, vuelva a intentarlo",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Usuario no creado ni guardado"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor, imposible realizar operación",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Error procesando la solicitud"
                    ))
            )
    })
    public Usuario guardar(@RequestBody Usuario usuario){
        return service.guardar(usuario);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un usuario mediante un id válido")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado, vuelva a intentarlo",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Usuario no encontrado"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor, imposible realizar operación",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Error procesando la solicitud"
                    ))
            )
    })
    public Usuario buscar(@PathVariable long id){
        return service.buscar(id);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario mediante un id válido")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario eliminado satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no eliminado, vuelva a intentarlo",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Usuario no eliminado"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor, imposible realizar operación",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Error procesando la solicitud"
                    ))
            )
    })
    public void eliminar(@PathVariable long id){
        service.eliminar(id);
    }

}
