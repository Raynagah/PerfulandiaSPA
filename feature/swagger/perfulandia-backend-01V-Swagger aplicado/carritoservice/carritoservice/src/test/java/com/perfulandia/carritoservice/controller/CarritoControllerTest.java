package com.perfulandia.carritoservice.controller;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Producto;
import com.perfulandia.carritoservice.model.Usuario;
import com.perfulandia.carritoservice.service.CarritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoControllerTest {

    @Mock
    private CarritoService carritoService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CarritoController carritoController;

    private Carrito carrito;
    private Producto perfume;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Configurar URLs de prueba
        carritoController.setProductosServiceUrl("http://localhost:8081");
        carritoController.setUsuariosServiceUrl("http://localhost:8082");

        // Datos de prueba
        perfume = Producto.builder()
                .id(1L)
                .nombre("Flor de Cerezo")
                .precio(89990)
                .build();

        usuario = new Usuario(100L, "Rodrigo Vargas", "ro.vargas@perfulandia.com", "CLIENTE");

        List<Long> productosIds = new ArrayList<>(List.of(1L));
        List<Producto> productos = new ArrayList<>(List.of(perfume));

        carrito = Carrito.builder()
                .id(1L)
                .usuarioId(100L)
                .productosIds(productosIds)
                .productos(productos)
                .build();
    }

    @Test
    void obtenerCarritoExistenteYRetornarOk() {
        when(carritoService.obtenerCarritoPorUsuario(100L)).thenReturn(carrito);

        ResponseEntity<CarritoController.CarritoResponse> response =
                carritoController.obtenerCarrito(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getProductosIds().size());
    }

    @Test
    void agregarProductoExistenteYRetornarOk() {
        when(restTemplate.getForObject(anyString(), eq(Producto.class)))
                .thenReturn(perfume);
        when(carritoService.agregarProducto(anyLong(), any(Producto.class)))
                .thenReturn(carrito);

        ResponseEntity<?> response =
                carritoController.agregarProducto(100L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(CarritoController.CarritoResponse.class, response.getBody());
    }

    @Test
    void agregarProductoNoExistenteYRetornarProductoNoEncontrado() {
        when(restTemplate.getForObject(anyString(), eq(Producto.class)))
                .thenReturn(null);

        ResponseEntity<?> response =
                carritoController.agregarProducto(100L, 99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado", response.getBody());
    }

    @Test
    void eliminarProductoExistenteYRetornarOk() {
        when(carritoService.eliminarProducto(100L, 1L))
                .thenReturn(carrito);

        ResponseEntity<CarritoController.CarritoResponse> response =
                carritoController.eliminarProducto(100L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void eliminarProductoInexistenteYRetornarOk() {
        when(carritoService.eliminarProducto(100L, 99L))
                .thenReturn(carrito); // Devuelve el carrito sin cambios

        ResponseEntity<CarritoController.CarritoResponse> response =
                carritoController.eliminarProducto(100L, 99L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void obtenerResumenUsuarioExistenteYRetornarResumen() {
        when(carritoService.obtenerCarritoPorUsuario(100L)).thenReturn(carrito);
        when(restTemplate.getForObject(anyString(), eq(Usuario.class)))
                .thenReturn(usuario);

        ResponseEntity<String> response =
                carritoController.obtenerResumen(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Rodrigo Vargas"));
        assertTrue(response.getBody().contains("Flor de Cerezo"));
    }

    @Test
    void obtenerResumenUsuarioNoExistenteYRetornarNoEncontrado() {
        when(carritoService.obtenerCarritoPorUsuario(100L)).thenReturn(carrito);
        when(restTemplate.getForObject(anyString(), eq(Usuario.class)))
                .thenReturn(null);

        ResponseEntity<String> response =
                carritoController.obtenerResumen(100L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void vaciarCarritoYRetornarOk() {
        doNothing().when(carritoService).vaciarCarrito(100L);

        ResponseEntity<?> response = carritoController.vaciarCarrito(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(carritoService, times(1)).vaciarCarrito(100L);
    }
}