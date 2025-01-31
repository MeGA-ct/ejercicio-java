package com.example.ejercicio_java.controller;

import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.service.LibroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroService;

    private final List<LibroDTO> libros = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        for (int i = 0; i < 2; i++) {
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

        mockMvc.perform(get("/libros").contentType(MediaType.APPLICATION_JSON))
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
}

