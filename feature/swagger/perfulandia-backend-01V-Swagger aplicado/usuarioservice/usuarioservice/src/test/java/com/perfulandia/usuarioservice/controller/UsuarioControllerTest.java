package com.perfulandia.usuarioservice.controller;

import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioService serviceMock;

    @InjectMocks
    private UsuarioController controller;

    private final Usuario usuario = Usuario.builder()
            .id(1L)
            .nombre("Rodrigo Vargas")
            .correo("ro.vargas@perfulandia.com")
            .rol("USUARIO")
            .build();

    @Test
    void listarUsuariosYRetornarLista() {
        when(serviceMock.listar()).thenReturn(Collections.singletonList(usuario));

        List<Usuario> resultado = controller.listar();

        assertEquals(1, resultado.size());
        assertEquals("USUARIO", resultado.get(0).getRol());
    }

    @Test
    void guardarUsuarioYRetornarUsuarioCreado() {
        when(serviceMock.guardar(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = controller.guardar(usuario);

        assertEquals("Rodrigo Vargas", resultado.getNombre());
    }

    @Test
    void buscarUsuarioPorIdYRetornarUsuario() {
        when(serviceMock.buscar(1L)).thenReturn(usuario);

        Usuario resultado = controller.buscar(1L);

        assertEquals("ro.vargas@perfulandia.com", resultado.getCorreo());
    }

    @Test
    void eliminarUsuarioYLlamarAlServicio() {
        controller.eliminar(1L);
        verify(serviceMock, times(1)).eliminar(1L);
    }
}