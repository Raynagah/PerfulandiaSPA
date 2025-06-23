package com.perfulandia.productservice.controller;

import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.model.Usuario;
import com.perfulandia.productservice.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    @Mock
    private ProductoService servicioMock;

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private ProductoController controller;

    private final Producto perfume = Producto.builder()
            .id(1L)
            .nombre("Azzaro The Most Wanted")
            .precio(80990)
            .stock(10)
            .build();

    private final Usuario usuario = new Usuario(1L, "Rodrigo Vargas", "ro.vargas@perfulandia.com", "CLIENTE");

    // --- Pruebas básicas ---
    //Listamos y luego retornamos los perfumes existentes
    @Test
    void listarYRetornarPerfumes() {
        when(servicioMock.listar()).thenReturn(List.of(perfume));

        List<Producto> resultado = controller.listar();

        assertEquals(1, resultado.size());
        assertEquals("Azzaro The Most Wanted", resultado.get(0).getNombre());
    }

    //Guardamos y luego retornamos el perfume creado
    @Test
    void guardarYRetornarPerfumeCreado() {
        when(servicioMock.guardar(any(Producto.class))).thenReturn(perfume);

        Producto resultado = controller.guardar(perfume);

        assertEquals(1L, resultado.getId());
    }

    //Obtenemos y retornamos el usuario con id 1
    @Test
    void obtenerUsuarioYRetornarlo() {
        when(restTemplateMock.getForObject("http://localhost:8081/api/usuarios/1", Usuario.class))
                .thenReturn(usuario);

        Usuario resultado = controller.obtenerUsuario(1L);

        assertEquals("Rodrigo Vargas", resultado.getNombre());
    }
}