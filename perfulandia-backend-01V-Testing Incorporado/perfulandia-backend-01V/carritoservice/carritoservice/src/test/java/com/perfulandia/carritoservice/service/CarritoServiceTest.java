package com.perfulandia.carritoservice.service;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CarritoService carritoService;

    private Carrito carrito;
    private Producto perfume1, perfume2;

    @BeforeEach
    void setUp() {
        perfume1 = Producto.builder()
                .id(1L)
                .nombre("Flor de Cerezo")
                .precio(89990)
                .stock(10)
                .build();

        perfume2 = Producto.builder()
                .id(2L)
                .nombre("Brisa Marina")
                .precio(89990)
                .stock(20)
                .build();

        // Usamos ArrayList en lugar de Arrays.asList para permitir modificaciones
        List<Long> productosIds = new ArrayList<>();
        productosIds.add(1L);
        productosIds.add(2L);

        List<Producto> productos = new ArrayList<>();
        productos.add(perfume1);
        productos.add(perfume2);

        carrito = Carrito.builder()
                .id(1L)
                .usuarioId(100L)
                .productosIds(productosIds)
                .productos(productos)
                .build();
    }

    @Test
    void agregarProductoValidoYAgregaAmbasListas() {
        // 1. Configurar carrito inicial con 2 productos
        Carrito carritoInicial = Carrito.builder()
                .id(1L)
                .usuarioId(100L)
                .productosIds(new ArrayList<>(List.of(1L, 2L)))
                .productos(new ArrayList<>(List.of(perfume1, perfume2)))
                .build();

        // 2. Configurar mocks
        when(carritoRepository.findByUsuarioId(100L)).thenReturn(carritoInicial);

        // Usamos ArgumentCaptor para verificar el objeto guardado
        ArgumentCaptor<Carrito> carritoCaptor = ArgumentCaptor.forClass(Carrito.class);
        when(carritoRepository.save(carritoCaptor.capture())).thenReturn(carritoInicial);

        // 3. Nuevo producto a agregar
        Producto nuevoPerfume = Producto.builder()
                .id(3L)
                .nombre("Aroma Fresco")
                .precio(65.00)
                .build();

        // 4. Ejecutar
        Carrito resultado = carritoService.agregarProducto(100L, nuevoPerfume);

        // 5. Verificar
        Carrito carritoGuardado = carritoCaptor.getValue();

        // Verificar IDs
        assertEquals(3, carritoGuardado.getProductosIds().size(),
                "Deberían haber 3 IDs de productos");
        assertTrue(carritoGuardado.getProductosIds().contains(3L),
                "Debería contener el ID del nuevo producto");

        // Verificar productos completos
        assertEquals(3, carritoGuardado.getProductos().size(),
                "Deberían haber 3 objetos Producto");
        assertTrue(carritoGuardado.getProductos().contains(nuevoPerfume),
                "Debería contener el nuevo producto");

        // Verificar sincronización
        assertEquals(
                carritoGuardado.getProductosIds().size(),
                carritoGuardado.getProductos().size(),
                "Ambas listas deberían tener el mismo tamaño"
        );
    }

    @Test
    void agregarProductoDuplicadoNoLoAgrega() {
        // Configuramos un carrito con un solo producto
        Carrito carritoConUnProducto = Carrito.builder()
                .usuarioId(100L)
                .productosIds(new ArrayList<>(List.of(1L)))
                .productos(new ArrayList<>(List.of(perfume1)))
                .build();

        when(carritoRepository.findByUsuarioId(100L)).thenReturn(carritoConUnProducto);

        Carrito resultado = carritoService.agregarProducto(100L, perfume1);

        assertEquals(1, resultado.getProductos().size());
    }

    @Test
    void eliminarProductoExistenteYEliminaDelCarrito() {
        // Configuramos un carrito clonado para modificación
        Carrito carritoModificable = Carrito.builder()
                .id(1L)
                .usuarioId(100L)
                .productosIds(new ArrayList<>(carrito.getProductosIds()))
                .productos(new ArrayList<>(carrito.getProductos()))
                .build();

        when(carritoRepository.findByUsuarioId(100L)).thenReturn(carritoModificable);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carritoModificable);

        Carrito resultado = carritoService.eliminarProducto(100L, 1L);

        assertEquals(1, resultado.getProductos().size());
        assertFalse(resultado.getProductosIds().contains(1L));
    }

    @Test
    void eliminarProductoInexistenteYNoCambiaCarrito() {
        // 1. Configurar carrito con 2 productos
        List<Long> ids = new ArrayList<>(List.of(1L, 2L));
        List<Producto> productos = new ArrayList<>(List.of(perfume1, perfume2));

        Carrito carritoInicial = Carrito.builder()
                .id(1L)
                .usuarioId(100L)
                .productosIds(ids)
                .productos(productos)
                .build();

        // 2. Configurar mock
        when(carritoRepository.findByUsuarioId(100L)).thenReturn(carritoInicial);

        // 3. Ejecutar con un ID que no existe
        Carrito resultado = carritoService.eliminarProducto(100L, 99L);

        // 4. Verificaciones
        assertNotNull(resultado, "El carrito no debería ser null");
        assertEquals(2, resultado.getProductosIds().size(), "Debería mantener 2 IDs");
        assertEquals(2, resultado.getProductos().size(), "Debería mantener 2 productos");

        // Verificar que no se llamó a save (pues no hubo cambios)
        verify(carritoRepository, never()).save(any());
    }

    @Test
    void vaciarCarritoYEliminarTodosLosProductos() {
        // Configuramos un carrito clonado
        Carrito carritoModificable = Carrito.builder()
                .id(1L)
                .usuarioId(100L)
                .productosIds(new ArrayList<>(carrito.getProductosIds()))
                .productos(new ArrayList<>(carrito.getProductos()))
                .build();

        when(carritoRepository.findByUsuarioId(100L)).thenReturn(carritoModificable);

        carritoService.vaciarCarrito(100L);

        verify(carritoRepository).save(argThat(c ->
                c.getProductosIds().isEmpty() && c.getProductos().isEmpty()
        ));
    }
}