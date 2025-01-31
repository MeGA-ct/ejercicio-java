package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroServiceImpl libroService;

    private final List<LibroDAO> libros = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        for (int i = 0; i < 3; i++) {
            LibroDAO libro = new LibroDAO(
                    "Libro " + i,
                    "Autor " + i,
                    "isbn " + i,
                    LocalDate.now().plusDays(i)
            );
            libros.add(libro);
        }
    }

    @Test
    void obtenerLibros() {

        when(libroRepository.findAll()).thenReturn(libros);

        List<LibroDTO> resultado = libroService.obtenerLibros();

        assertEquals(3, resultado.size());
    }
}