package com.perfulandia.productservice.controller;
import com.perfulandia.productservice.model.Usuario;
import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.service.ProductoService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//Nuevas importaciones DTO conexión al MS usuario
import org.springframework.web.client.RestTemplate;
//Para hacer peticiones HTTP a otros microservicios.


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@Tag(name="Productos",description = "Gestión de productos de la tienda") //Anotación para Swagger :D
public class ProductoController {



    private final ProductoService servicio;
    private final RestTemplate restTemplate;
    public ProductoController(ProductoService servicio,  RestTemplate restTemplate){
        this.servicio = servicio;
        this.restTemplate = restTemplate;
    }

    //listar
    @GetMapping
    @Operation(summary="Listar productos") //Descripción del endpoint
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de productos realizado satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Lista no encontrada",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Producto No encontrado"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor, imposible listar productos",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Error procesando la solicitud"
                    ))
            )
    })
    public List<Producto> listar() {
        return servicio.listar();
    }

    //guardar
    @PostMapping
    @Operation(summary = "Crear y guardar un producto")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto creado y guardado satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no creado ni guardado",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Producto no guardado"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Error procesando la solicitud"
                    ))
            )
    })
    public Producto guardar(@RequestBody Producto producto){
        return servicio.guardar(producto);
    }


    //buscar x id
    @GetMapping("/{id}")
    @Operation(summary = "Buscar un producto mediante su id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Producto No encontrado"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Error procesando la solicitud"
                    ))
            )
    })
    public ResponseEntity<Producto> buscar(@PathVariable long id) {
        Producto producto = servicio.bucarPorId(id);
        return ResponseEntity.ok(producto);
    }

    //Eliminar
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto mediante su id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto eliminado satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado, intente con un producto válido",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Producto No encontrado"
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
        servicio.eliminar(id);
    }

    //Nuevo metodo
    @GetMapping("/usuario/{id}")
    @Operation(summary = "Obtener un usuario mediante su id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado, intente nuevamente",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Usuario No encontrado"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor, imposible buscar usuarios",
                    content = @Content(schema = @Schema(
                            implementation = String.class,
                            example = "Error procesando la solicitud"
                    ))
            )
    })
    public Usuario obtenerUsuario(@PathVariable long id){
        return restTemplate.getForObject("http://localhost:8081/api/usuarios/"+id,Usuario.class);
    }

    @GetMapping("/batch")
    @Operation(summary = "Obtener productos por sus ids")
    public List<Producto> obtenerProductosPorIds(@RequestParam String ids) {
        List<Long> idsList = Arrays.stream(ids.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        return servicio.buscarTodosPorIds(idsList);
    }


}
