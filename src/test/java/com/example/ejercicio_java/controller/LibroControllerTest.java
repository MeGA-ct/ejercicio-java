package com.example.ejercicio_java.controller;

import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.service.LibroService;
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
@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvcC;

    @MockBean
    private LibroService libroService;

    private final List<LibroDTO> libros = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        for (int i = 1; i <= 2; i++) {
            LibroDTO libro = new LibroDTO(
                    (long) i,
                    "Libro " + i,
                    "Autor " + i,
                    "isbn " + i,
                    LocalDate.now().plusDays(i)
            );
            libros.add(libro);
        }
    }

    @Test
    void testObtenerLibros() throws Exception {
        when(libroService.obtenerLibros()).thenReturn(libros);

        mockMvcC.perform(get("/libros").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(libros.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].titulo", is(libros.get(0).getTitulo())))
                .andExpect(jsonPath("$[0].autor", is(libros.get(0).getAutor())))
                .andExpect(jsonPath("$[0].isbn", is(libros.get(0).getIsbn())))
                .andExpect(jsonPath("$[1].id", is(libros.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].titulo", is(libros.get(1).getTitulo())))
                .andExpect(jsonPath("$[1].autor", is(libros.get(1).getAutor())))
                .andExpect(jsonPath("$[1].isbn", is(libros.get(1).getIsbn())));

        verify(libroService, times(1)).obtenerLibros();
    }

    @Test
    void testObtenerUnLibro() throws Exception {
        when(libroService.obtenerUnLibro(any(Long.class))).thenReturn(libros.get(0));
        mockMvcC.perform(get("/libros/0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(libros.get(0).getId().intValue())))
                .andExpect(jsonPath("$.titulo", is(libros.get(0).getTitulo())))
                .andExpect(jsonPath("$.autor", is(libros.get(0).getAutor())))
                .andExpect(jsonPath("$.isbn", is(libros.get(0).getIsbn())));
    }

    @Test
    void testObtenerUnLibroNotFound() throws Exception {
        when(libroService.obtenerUnLibro(any(Long.class))).thenThrow(LibroException.class);
        mockMvcC.perform(get("/libros/0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGuardarLibro() throws Exception {
        when(libroService.guardarLibro(any(LibroDTO.class))).thenReturn(libros.get(0));
        mockMvcC.perform(
                        post("/libros")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{" +
                                        " \"titulo\":\"" + libros.get(0).getTitulo() + "\"," +
                                        " \"autor\":\"" + libros.get(0).getAutor() + "\"," +
                                        " \"isbn\":\"" + libros.get(0).getIsbn() + "\"," +
                                        " \"fechaPublicacion\":\"" + libros.get(0).getFechaPublicacion() + "\"" +
                                        "}"
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(libros.get(0).getId().intValue())))
                .andExpect(jsonPath("$.titulo", is(libros.get(0).getTitulo())))
                .andExpect(jsonPath("$.autor", is(libros.get(0).getAutor())))
                .andExpect(jsonPath("$.isbn", is(libros.get(0).getIsbn())));
    }

    @Test
    void testGuardarLibroError() throws Exception {
        when(libroService.guardarLibro(any(LibroDTO.class))).thenThrow(LibroException.class);
        mockMvcC.perform(post("/libros").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarLibro() throws Exception {
        when(libroService.actualizarLibro(any(LibroDTO.class))).thenReturn(libros.get(0));
        mockMvcC.perform(
                        put("/libros/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{" +
                                        " \"titulo\":\"" + libros.get(0).getTitulo() + "\"," +
                                        " \"autor\":\"" + libros.get(0).getAutor() + "\"," +
                                        " \"isbn\":\"" + libros.get(0).getIsbn() + "\"," +
                                        " \"fechaPublicacion\":\"" + libros.get(0).getFechaPublicacion() + "\"" +
                                        "}"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(libros.get(0).getId().intValue())))
                .andExpect(jsonPath("$.titulo", is(libros.get(0).getTitulo())))
                .andExpect(jsonPath("$.autor", is(libros.get(0).getAutor())))
                .andExpect(jsonPath("$.isbn", is(libros.get(0).getIsbn())));
    }

    @Test
    void testActualizarLibroError() throws Exception {
        when(libroService.actualizarLibro(any(LibroDTO.class))).thenThrow(LibroException.class);
        mockMvcC.perform(put("/libros/9").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarParcialmenteLibro() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("titulo", "Nuevo Título");
        updates.put("autor", "Nuevo Autor");

        when(libroService.actualizarParcialmenteLibro(any(Long.class), anyMap())).thenReturn(libros.get(0));
        mockMvcC.perform(patch("/libros/1")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(new ObjectMapper().writeValueAsString(updates))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(libros.get(0).getId().intValue())))
                .andExpect(jsonPath("$.titulo", is(libros.get(0).getTitulo())))
                .andExpect(jsonPath("$.autor", is(libros.get(0).getAutor())))
                .andExpect(jsonPath("$.isbn", is(libros.get(0).getIsbn())));
    }

    @Test
    void testActualizarParcialmenteLibroError() throws Exception {
        when(libroService.actualizarParcialmenteLibro(any(Long.class), anyMap())).thenThrow(LibroException.class);
        mockMvcC.perform(patch("/libros/9").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBorrarLibro() throws Exception {
        Long libroId = 1L;

        doNothing().when(libroService).borrarLibro(libroId);

        mockMvcC.perform(delete("/libros/{id}", libroId)
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());

        verify(libroService, times(1)).borrarLibro(libroId);
    }

    @Test
    void testBorrarLibroError() throws Exception {
        Long libroId = 9L;
        String mensajeError = "Libro no encontrado";

        doThrow(new LibroException(501, mensajeError)).when(libroService).borrarLibro(libroId);

        mockMvcC.perform(delete("/libros/9").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}

