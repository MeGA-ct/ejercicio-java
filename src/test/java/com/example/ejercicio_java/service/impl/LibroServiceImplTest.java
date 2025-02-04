package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroServiceImpl libroService;

    List<LibroDAO> libros = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        for (int i = 0; i < 3; i++) {
            LibroDAO libro = new LibroDAO(
                    "Libro " + i,
                    "Autor " + i,
                    "isbn " + i,
                    LocalDate.now().plusDays(i)
            );
            libro.setId((long) i);
            libros.add(libro);
        }
    }

    @Test
    void testObtenerLibros() {

        when(libroRepository.findAll()).thenReturn(libros);

        List<LibroDTO> resultado = libroService.obtenerLibros();

        assertEquals(3, resultado.size());
    }

    @Test
    void testObtenerUnLibro() {
        int indiceLista = 0;
        LibroDAO unLibro = libros.get(indiceLista);
        unLibro.setId((long) indiceLista); //Simulated save

        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.of(unLibro));

        LibroDTO resultado = libroService.obtenerUnLibro(unLibro.getId());

        assertEquals(unLibro.getTitulo(), resultado.getTitulo());
    }

    @Test
    void testObtenerUnLibroNotFound() {
        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(LibroException.class, () -> libroService.obtenerUnLibro(1L));
    }
}