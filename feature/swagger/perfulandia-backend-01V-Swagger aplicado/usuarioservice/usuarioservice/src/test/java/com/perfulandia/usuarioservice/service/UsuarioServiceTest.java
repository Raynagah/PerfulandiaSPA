package com.perfulandia.usuarioservice.service;

import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repoMock;

    @InjectMocks
    private UsuarioService servicio;

    // Datos de ejemplo
    private final Usuario usuario1 = Usuario.builder()
            .id(1L)
            .nombre("Rodrigo Vargas")
            .correo("ro.vargas@perfulandia.com")
            .rol("ADMIN")
            .build();

    private final Usuario usuario2 = Usuario.builder()
            .id(2L)
            .nombre("Martin Baza")
            .correo("ma.baza@perfulandia.com")
            .rol("GERENTE")
            .build();

    @Test
    void listarUsuariosYRetornaListaDeUsuarios() {
        when(repoMock.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<Usuario> usuarios = servicio.listar();

        assertEquals(2, usuarios.size());
        assertEquals("ADMIN", usuarios.get(0).getRol());
        verify(repoMock, times(1)).findAll();
    }

    @Test
    void guardarUsuarioYRetornaUsuarioGuardado() {
        when(repoMock.save(any(Usuario.class))).thenReturn(usuario1);

        Usuario resultado = servicio.guardar(usuario1);

        assertEquals(1L, resultado.getId());
        assertEquals("Rodrigo Vargas", resultado.getNombre());
    }

    @Test
    void buscarUsuarioPorIdExistenteYRetornaUsuario() {
        when(repoMock.findById(1L)).thenReturn(Optional.of(usuario1));

        Usuario resultado = servicio.buscar(1L);

        assertNotNull(resultado);
        assertEquals("ro.vargas@perfulandia.com", resultado.getCorreo());
    }

    @Test
    void buscarUsuarioPorIdInexistenteYRetornaNull() {
        when(repoMock.findById(99L)).thenReturn(Optional.empty());

        Usuario resultado = servicio.buscar(99L);

        assertNull(resultado);
    }

    @Test
    void eliminarUsuarioYLlamarAlRepositorio() {
        servicio.eliminar(1L);
        verify(repoMock, times(1)).deleteById(1L);
    }
}