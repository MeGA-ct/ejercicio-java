package com.example.ejercicio_java.controller;

import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.dto.PrestamoDTO;
import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.exceptions.prestamo.PrestamoException;
import com.example.ejercicio_java.exceptions.usuario.UsuarioException;
import com.example.ejercicio_java.service.LibroService;
import com.example.ejercicio_java.service.PrestamoService;
import com.example.ejercicio_java.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PrestamoController.class)
class PrestamoControllerTest {

    private static final String PRESTAMOS_PATH = "/prestamos";
    private static final String PRESTAMOS_PATH_ID = PRESTAMOS_PATH + "/%d";

    @Autowired
    private MockMvc mockMvcC;

    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private LibroService libroService;
    @MockBean
    private PrestamoService prestamoService;

    private final List<PrestamoDTO> prestamoDTOList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        for (int i = 1; i <= 2; i++) {
            PrestamoDTO prestamoDTO = new PrestamoDTO(
                    (long) i,
                    new UsuarioDTO(
                            (long) i,
                            "Usuario " + i,
                            "email_" + i + "@mail.mail",
                            String.format("700%07d", i),
                            LocalDate.now().minusDays(i)
                    ),
                    new LibroDTO(
                            (long) i,
                            "Libro " + i,
                            "Autor " + i,
                            "isbn " + i,
                            LocalDate.now().minusYears(i)
                    ),
                    LocalDate.now().minusDays(7 * i),
                    LocalDate.now()
            );
            prestamoDTOList.add(prestamoDTO);
        }
    }

    @Test
    void testObtenerPrestamos() throws Exception {
        when(prestamoService.obtenerPrestamos()).thenReturn(prestamoDTOList);

        mockMvcC.perform(get(PRESTAMOS_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(prestamoDTOList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].usuario.id", is(prestamoDTOList.get(0).getUsuario().getId().intValue())))
                .andExpect(jsonPath("$[0].usuario.nombre", is(prestamoDTOList.get(0).getUsuario().getNombre())))
                .andExpect(jsonPath("$[0].usuario.telefono", is(prestamoDTOList.get(0).getUsuario().getTelefono())))
                .andExpect(jsonPath(
                        "$[0].usuario.fechaRegistro",
                        is(prestamoDTOList.get(0).getUsuario().getFechaRegistro().toString())
                ))
                .andExpect(jsonPath("$[0].libro.id", is(prestamoDTOList.get(0).getLibro().getId().intValue())))
                .andExpect(jsonPath("$[0].libro.titulo", is(prestamoDTOList.get(0).getLibro().getTitulo())))
                .andExpect(jsonPath("$[0].libro.isbn", is(prestamoDTOList.get(0).getLibro().getIsbn())))
                .andExpect(jsonPath(
                        "$[0].libro.fechaPublicacion",
                        is(prestamoDTOList.get(0).getLibro().getFechaPublicacion().toString())
                ))
                .andExpect(jsonPath("$[1].id", is(prestamoDTOList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].usuario.id", is(prestamoDTOList.get(1).getUsuario().getId().intValue())))
                .andExpect(jsonPath("$[1].usuario.nombre", is(prestamoDTOList.get(1).getUsuario().getNombre())))
                .andExpect(jsonPath("$[1].usuario.telefono", is(prestamoDTOList.get(1).getUsuario().getTelefono())))
                .andExpect(jsonPath(
                        "$[1].usuario.fechaRegistro",
                        is(prestamoDTOList.get(1).getUsuario().getFechaRegistro().toString())
                ))
                .andExpect(jsonPath("$[1].libro.id", is(prestamoDTOList.get(1).getLibro().getId().intValue())))
                .andExpect(jsonPath("$[1].libro.titulo", is(prestamoDTOList.get(1).getLibro().getTitulo())))
                .andExpect(jsonPath("$[1].libro.isbn", is(prestamoDTOList.get(1).getLibro().getIsbn())))
                .andExpect(jsonPath(
                        "$[1].libro.fechaPublicacion",
                        is(prestamoDTOList.get(1).getLibro().getFechaPublicacion().toString())
                ));

        verify(prestamoService, times(1)).obtenerPrestamos();
    }

    @Test
    void testObtenerUnPrestamo() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, prestamoDTO.getId());

        when(prestamoService.obtenerUnPrestamo(any(Long.class))).thenReturn(prestamoDTO);

        mockMvcC.perform(get(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(prestamoDTO.getId().intValue())))
                .andExpect(jsonPath("$.usuario.id", is(prestamoDTO.getUsuario().getId().intValue())))
                .andExpect(jsonPath("$.usuario.nombre", is(prestamoDTO.getUsuario().getNombre())))
                .andExpect(jsonPath("$.usuario.telefono", is(prestamoDTO.getUsuario().getTelefono())))
                .andExpect(jsonPath(
                        "$.usuario.fechaRegistro",
                        is(prestamoDTO.getUsuario().getFechaRegistro().toString())
                ))
                .andExpect(jsonPath("$.libro.id", is(prestamoDTO.getLibro().getId().intValue())))
                .andExpect(jsonPath("$.libro.titulo", is(prestamoDTO.getLibro().getTitulo())))
                .andExpect(jsonPath("$.libro.isbn", is(prestamoDTO.getLibro().getIsbn())))
                .andExpect(jsonPath(
                        "$.libro.fechaPublicacion",
                        is(prestamoDTO.getLibro().getFechaPublicacion().toString())
                ));

        verify(prestamoService, times(1)).obtenerUnPrestamo(any(Long.class));
    }

    @Test
    void testObtenerUnPrestamoNotFound() throws Exception {
        String path = String.format(PRESTAMOS_PATH_ID, 99);

        when(prestamoService.obtenerUnPrestamo(any(Long.class))).thenThrow(PrestamoException.class);

        mockMvcC.perform(get(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(prestamoService, times(1)).obtenerUnPrestamo(any(Long.class));
    }

    @Test
    void testGuardarPrestamo() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String cuerpo = "{" +
                        String.format("\"usuarioId\": %d,", prestamoDTO.getUsuario().getId()) +
                        String.format("\"libroId\": %d,", prestamoDTO.getLibro().getId()) +
                        String.format("\"fechaPrestamo\": \"%s\",", prestamoDTO.getFechaPrestamo().toString()) +
                        String.format("\"fechaDevolucion\": \"%s\"", prestamoDTO.getFechaDevolucion().toString()) +
                        "}";

        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenReturn(prestamoDTO.getUsuario());
        when(libroService.obtenerUnLibro(any(Long.class))).thenReturn(prestamoDTO.getLibro());
        when(prestamoService.guardarPrestamo(any(PrestamoDTO.class))).thenReturn(prestamoDTO);

        mockMvcC.perform(post(PRESTAMOS_PATH).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(prestamoDTO.getId().intValue())))
                .andExpect(jsonPath("$.usuario.id", is(prestamoDTO.getUsuario().getId().intValue())))
                .andExpect(jsonPath("$.usuario.nombre", is(prestamoDTO.getUsuario().getNombre())))
                .andExpect(jsonPath("$.usuario.telefono", is(prestamoDTO.getUsuario().getTelefono())))
                .andExpect(jsonPath(
                        "$.usuario.fechaRegistro",
                        is(prestamoDTO.getUsuario().getFechaRegistro().toString())
                ))
                .andExpect(jsonPath("$.libro.id", is(prestamoDTO.getLibro().getId().intValue())))
                .andExpect(jsonPath("$.libro.titulo", is(prestamoDTO.getLibro().getTitulo())))
                .andExpect(jsonPath("$.libro.isbn", is(prestamoDTO.getLibro().getIsbn())))
                .andExpect(jsonPath(
                        "$.libro.fechaPublicacion",
                        is(prestamoDTO.getLibro().getFechaPublicacion().toString())
                ));

        verify(usuarioService, times(1)).obtenerUnUsuario(any(Long.class));
        verify(libroService, times(1)).obtenerUnLibro(any(Long.class));
        verify(prestamoService, times(1)).guardarPrestamo(any(PrestamoDTO.class));
    }

    @Test
    void testGuardarPrestamoUsuarioNotFound() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String cuerpo = "{" +
                        String.format("\"usuarioId\": %d,", 0) +
                        String.format("\"libroId\": %d,", prestamoDTO.getLibro().getId()) +
                        String.format("\"fechaPrestamo\": \"%s\",", prestamoDTO.getFechaPrestamo().toString()) +
                        String.format("\"fechaDevolucion\": \"%s\"", prestamoDTO.getFechaDevolucion().toString()) +
                        "}";

        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenThrow(UsuarioException.class);

        mockMvcC.perform(post(PRESTAMOS_PATH).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest());

        verify(usuarioService, times(1)).obtenerUnUsuario(any(Long.class));
    }

    @Test
    void testGuardarPrestamoLibroNotFound() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String cuerpo = "{" +
                        String.format("\"usuarioId\": %d,", prestamoDTO.getUsuario().getId()) +
                        String.format("\"libroId\": %d,", 0) +
                        String.format("\"fechaPrestamo\": \"%s\",", prestamoDTO.getFechaPrestamo().toString()) +
                        String.format("\"fechaDevolucion\": \"%s\"", prestamoDTO.getFechaDevolucion().toString()) +
                        "}";

        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenReturn(prestamoDTO.getUsuario());
        when(libroService.obtenerUnLibro(any(Long.class))).thenThrow(LibroException.class);

        mockMvcC.perform(post(PRESTAMOS_PATH).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest());

        verify(usuarioService, times(1)).obtenerUnUsuario(any(Long.class));
        verify(libroService, times(1)).obtenerUnLibro(any(Long.class));
    }

    @Test
    void testActualizarPrestamo() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, prestamoDTO.getId());
        String cuerpo = "{" +
                        String.format("\"usuarioId\": %d,", prestamoDTO.getUsuario().getId()) +
                        String.format("\"libroId\": %d,", prestamoDTO.getLibro().getId()) +
                        String.format("\"fechaPrestamo\": \"%s\",", prestamoDTO.getFechaPrestamo().toString()) +
                        String.format("\"fechaDevolucion\": \"%s\"", prestamoDTO.getFechaDevolucion().toString()) +
                        "}";

        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenReturn(prestamoDTO.getUsuario());
        when(libroService.obtenerUnLibro(any(Long.class))).thenReturn(prestamoDTO.getLibro());
        when(prestamoService.actualizarPrestamo(any(PrestamoDTO.class))).thenReturn(prestamoDTO);

        mockMvcC.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(prestamoDTO.getId().intValue())))
                .andExpect(jsonPath("$.usuario.id", is(prestamoDTO.getUsuario().getId().intValue())))
                .andExpect(jsonPath("$.usuario.nombre", is(prestamoDTO.getUsuario().getNombre())))
                .andExpect(jsonPath("$.usuario.telefono", is(prestamoDTO.getUsuario().getTelefono())))
                .andExpect(jsonPath(
                        "$.usuario.fechaRegistro",
                        is(prestamoDTO.getUsuario().getFechaRegistro().toString())
                ))
                .andExpect(jsonPath("$.libro.id", is(prestamoDTO.getLibro().getId().intValue())))
                .andExpect(jsonPath("$.libro.titulo", is(prestamoDTO.getLibro().getTitulo())))
                .andExpect(jsonPath("$.libro.isbn", is(prestamoDTO.getLibro().getIsbn())))
                .andExpect(jsonPath(
                        "$.libro.fechaPublicacion",
                        is(prestamoDTO.getLibro().getFechaPublicacion().toString())
                ));

        verify(usuarioService, times(1)).obtenerUnUsuario(any(Long.class));
        verify(libroService, times(1)).obtenerUnLibro(any(Long.class));
        verify(prestamoService, times(1)).actualizarPrestamo(any(PrestamoDTO.class));
    }

    @Test
    void testActualizarPrestamoUsuarioNotFound() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, prestamoDTO.getId());
        String cuerpo = "{" +
                        String.format("\"usuarioId\": %d,", 0) +
                        String.format("\"libroId\": %d,", prestamoDTO.getLibro().getId()) +
                        String.format("\"fechaPrestamo\": \"%s\",", prestamoDTO.getFechaPrestamo().toString()) +
                        String.format("\"fechaDevolucion\": \"%s\"", prestamoDTO.getFechaDevolucion().toString()) +
                        "}";

        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenThrow(UsuarioException.class);

        mockMvcC.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest());

        verify(usuarioService, times(1)).obtenerUnUsuario(any(Long.class));
    }

    @Test
    void testActualizarPrestamoLibroNotFound() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, prestamoDTO.getId());
        String cuerpo = "{" +
                        String.format("\"usuarioId\": %d,", prestamoDTO.getUsuario().getId()) +
                        String.format("\"libroId\": %d,", 0) +
                        String.format("\"fechaPrestamo\": \"%s\",", prestamoDTO.getFechaPrestamo().toString()) +
                        String.format("\"fechaDevolucion\": \"%s\"", prestamoDTO.getFechaDevolucion().toString()) +
                        "}";

        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenReturn(prestamoDTO.getUsuario());
        when(libroService.obtenerUnLibro(any(Long.class))).thenThrow(LibroException.class);

        mockMvcC.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest());

        verify(usuarioService, times(1)).obtenerUnUsuario(any(Long.class));
        verify(libroService, times(1)).obtenerUnLibro(any(Long.class));
    }

    @Test
    void testActualizarPrestamoNotFound() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, 0);
        String cuerpo = "{" +
                        String.format("\"usuarioId\": %d,", prestamoDTO.getUsuario().getId()) +
                        String.format("\"libroId\": %d,", prestamoDTO.getLibro().getId()) +
                        String.format("\"fechaPrestamo\": \"%s\",", prestamoDTO.getFechaPrestamo().toString()) +
                        String.format("\"fechaDevolucion\": \"%s\"", prestamoDTO.getFechaDevolucion().toString()) +
                        "}";

        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenReturn(prestamoDTO.getUsuario());
        when(libroService.obtenerUnLibro(any(Long.class))).thenReturn(prestamoDTO.getLibro());
        when(prestamoService.actualizarPrestamo(any(PrestamoDTO.class))).thenThrow(PrestamoException.class);

        mockMvcC.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest());

        verify(usuarioService, times(1)).obtenerUnUsuario(any(Long.class));
        verify(libroService, times(1)).obtenerUnLibro(any(Long.class));
        verify(prestamoService, times(1)).actualizarPrestamo(any(PrestamoDTO.class));
    }

    @Test
    void testActualizarParcialmentePrestamo() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, prestamoDTO.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("usuarioId", prestamoDTO.getUsuario().getId());
        updates.put("libroId", prestamoDTO.getLibro().getId());
        updates.put("fechaPrestamo", prestamoDTO.getFechaPrestamo().toString());
        updates.put("fechaDevolucion", prestamoDTO.getFechaDevolucion().toString());

        when(prestamoService.actualizarParcialmentePrestamo(any(Long.class), anyMap())).thenReturn(prestamoDTO);

        mockMvcC.perform(patch(path).contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(prestamoDTO.getId().intValue())))
                .andExpect(jsonPath("$.usuario.id", is(prestamoDTO.getUsuario().getId().intValue())))
                .andExpect(jsonPath("$.usuario.nombre", is(prestamoDTO.getUsuario().getNombre())))
                .andExpect(jsonPath("$.usuario.telefono", is(prestamoDTO.getUsuario().getTelefono())))
                .andExpect(jsonPath(
                        "$.usuario.fechaRegistro",
                        is(prestamoDTO.getUsuario().getFechaRegistro().toString())
                ))
                .andExpect(jsonPath("$.libro.id", is(prestamoDTO.getLibro().getId().intValue())))
                .andExpect(jsonPath("$.libro.titulo", is(prestamoDTO.getLibro().getTitulo())))
                .andExpect(jsonPath("$.libro.isbn", is(prestamoDTO.getLibro().getIsbn())))
                .andExpect(jsonPath(
                        "$.libro.fechaPublicacion",
                        is(prestamoDTO.getLibro().getFechaPublicacion().toString())
                ));

        verify(prestamoService, times(1)).actualizarParcialmentePrestamo(any(Long.class), anyMap());
    }

    @Test
    void testActualizarParcialmentePrestamoUsuarioNotFound() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, prestamoDTO.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("usuarioId", 0);
        updates.put("libroId", prestamoDTO.getLibro().getId());
        updates.put("fechaPrestamo", prestamoDTO.getFechaPrestamo().toString());
        updates.put("fechaDevolucion", prestamoDTO.getFechaDevolucion().toString());

        when(prestamoService.actualizarParcialmentePrestamo(
                any(Long.class),
                anyMap()
        )).thenThrow(UsuarioException.class);

        mockMvcC.perform(patch(path).contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isBadRequest());

        verify(prestamoService, times(1)).actualizarParcialmentePrestamo(any(Long.class), anyMap());
    }

    @Test
    void testActualizarParcialmentePrestamoLibroNotFound() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, prestamoDTO.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("usuarioId", prestamoDTO.getLibro().getId());
        updates.put("libroId", 0);
        updates.put("fechaPrestamo", prestamoDTO.getFechaPrestamo().toString());
        updates.put("fechaDevolucion", prestamoDTO.getFechaDevolucion().toString());

        when(prestamoService.actualizarParcialmentePrestamo(any(Long.class), anyMap())).thenThrow(LibroException.class);

        mockMvcC.perform(patch(path).contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isBadRequest());

        verify(prestamoService, times(1)).actualizarParcialmentePrestamo(any(Long.class), anyMap());
    }

    @Test
    void testActualizarParcialmentePrestamoNotFound() throws Exception {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        String path = String.format(PRESTAMOS_PATH_ID, 0);

        Map<String, Object> updates = new HashMap<>();
        updates.put("usuarioId", "");
        updates.put("libroId", "");
        updates.put("fechaPrestamo", prestamoDTO.getFechaPrestamo().toString());
        updates.put("fechaDevolucion", prestamoDTO.getFechaDevolucion().toString());

        when(prestamoService.actualizarParcialmentePrestamo(
                any(Long.class),
                anyMap()
        )).thenThrow(PrestamoException.class);

        mockMvcC.perform(patch(path).contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isBadRequest());

        verify(prestamoService, times(1)).actualizarParcialmentePrestamo(any(Long.class), anyMap());
    }

    @Test
    void testBorrarPrestamo() throws Exception {
        Long prestamoId = prestamoDTOList.get(0).getId();
        String path = String.format(PRESTAMOS_PATH_ID, prestamoId);

        doNothing().when(prestamoService).borrarPrestamo(prestamoId);

        mockMvcC.perform(delete(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(prestamoService, times(1)).borrarPrestamo(prestamoId);
    }

    @Test
    void testBorrarPrestamoError() throws Exception {
        Long prestamoId = 0L;
        String path = String.format(PRESTAMOS_PATH_ID, prestamoId);
        String mensajeError = "Préstamo no encontrado";

        doThrow(new PrestamoException(PrestamoException.NO_ENCONTRADO, mensajeError))
                .when(prestamoService)
                .borrarPrestamo(prestamoId);

        mockMvcC.perform(delete(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(prestamoService, times(1)).borrarPrestamo(prestamoId);
    }
}