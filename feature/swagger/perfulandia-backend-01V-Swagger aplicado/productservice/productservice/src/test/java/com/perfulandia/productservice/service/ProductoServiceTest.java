package com.perfulandia.productservice.service;


import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Habilita Mockito
public class ProductoServiceTest {

    @Mock
    private ProductoRepository repoMock;  // Simulamos el repositorio

    @InjectMocks
    private ProductoService servicio;  // Inyectamos el mock en el servicio

    // Datos de ejemplo para el testeo
    private final Producto perfume1 = Producto.builder()
            .id(1L)
            .nombre("Azzaro The Most Wanted")
            .precio(80990)
            .stock(25)
            .build();

    private final Producto perfume2 = Producto.builder()
            .id(2L)
            .nombre("Azzaro Wanted By Night")
            .precio(74990)
            .stock(20)
            .build();

    // --- Pruebas básicas ---
    //Lista los productos y luego retorna la lista de perfumes
    @Test
    void listarProductosExistentes() {
        // Configurar el mock
        when(repoMock.findAll()).thenReturn(Arrays.asList(perfume1, perfume2));

        // Ejecutar el metodo a probar
        List<Producto> productos = servicio.listar();

        // Verificar resultados
        assertEquals(2, productos.size());
        assertEquals("Azzaro The Most Wanted", productos.get(0).getNombre());
        verify(repoMock, times(1)).findAll();  // Asegura que se llamó al repo
    }

    //Guarda un producto y luego retorna el perfume guardado
    @Test
    void guardarProductoyRetornarlo() {
        when(repoMock.save(any(Producto.class))).thenReturn(perfume1);

        Producto resultado = servicio.guardar(perfume1);

        assertEquals(1L, resultado.getId());
        assertEquals(80990, resultado.getPrecio());
    }

    //Busca un producto, si encuentra su id lo retorna
    @Test
    void buscarPorIdExistente() {
        when(repoMock.findById(1L)).thenReturn(java.util.Optional.of(perfume1));

        Producto resultado = servicio.bucarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Azzaro The Most Wanted", resultado.getNombre());
    }

    //Elimina un producto por su id y verificar la llamada al metodo
    @Test
    void eliminarProductoYLlamarAlRepositorio() {
        servicio.eliminar(1L);
        verify(repoMock, times(1)).deleteById(1L);  // Solo verificamos que se llamó al metodo
    }
}