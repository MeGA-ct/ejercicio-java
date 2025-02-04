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
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.ejercicio_java.service.impl.LibroServiceImpl.LIBRO_ISBN_DUPLICADO_MENSAJE;
import static com.example.ejercicio_java.service.impl.LibroServiceImpl.LIBRO_NO_ENCONTRADO_MENSAJE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroServiceImpl libroService;

    List<LibroDAO> librosDao = new ArrayList<>();

    List<LibroDTO> librosDto = new ArrayList<>();

    LibroException errorLibroNoEncontrado;
    LibroException errorISBNDuplicado;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        for (int i = 1; i <= 3; i++) {
            LibroDAO libroDao = new LibroDAO(
                    "Libro " + i,
                    "Autor " + i,
                    "isbn " + i,
                    LocalDate.now().minusDays(i)
            );
            libroDao.setId((long) i);
            librosDao.add(libroDao);

            LibroDTO libroDto = new LibroDTO(
                    (long) i,
                    "Libro " + i,
                    "Autor " + i,
                    "isbn " + i,
                    LocalDate.now().minusDays(i)
            );
            librosDto.add(libroDto);
        }
        errorLibroNoEncontrado = new LibroException(
                500,
                String.format(LIBRO_NO_ENCONTRADO_MENSAJE, librosDto.get(0).getId())
        );
        errorISBNDuplicado = new LibroException(
                501,
                String.format(LIBRO_ISBN_DUPLICADO_MENSAJE, librosDto.get(0).getIsbn())
        );
    }

    @Test
    void testObtenerLibros() {

        when(libroRepository.findAll()).thenReturn(librosDao);

        List<LibroDTO> resultado = libroService.obtenerLibros();

        assertEquals(3, resultado.size());
    }

    @Test
    void testObtenerUnLibro() {
        int indiceLista = 0;
        LibroDAO unLibro = librosDao.get(indiceLista);
        unLibro.setId((long) indiceLista); //Guardado simulado

        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.of(unLibro));

        LibroDTO resultado = libroService.obtenerUnLibro(unLibro.getId());

        assertEquals(unLibro.getTitulo(), resultado.getTitulo());
    }

    @Test
    void testObtenerUnLibroNotFound() {
        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        LibroException exception = assertThrows(LibroException.class, () -> libroService.obtenerUnLibro(1L));

        assertEquals(errorLibroNoEncontrado.getMessage(), exception.getMessage());
    }

    @Test
    void testGuardarLibro() {
        int indiceLista = 0;
        LibroDTO unLibro = librosDto.get(indiceLista);

        when(libroRepository.save(any(LibroDAO.class))).thenReturn(librosDao.get(indiceLista));

        LibroDTO resultado = libroService.guardarLibro(unLibro);

        assertEquals(unLibro.getTitulo(), resultado.getTitulo());
    }

    @Test
    void testGuardarLibroISBNDuplicado() {
        LibroDTO unLibro = librosDto.get(0);
        LibroException errorEsperado = new LibroException(
                501,
                String.format(LIBRO_ISBN_DUPLICADO_MENSAJE, librosDto.get(0).getIsbn())
        );

        when(libroRepository.save(any(LibroDAO.class)))
                .thenThrow(new DataIntegrityViolationException("ISBN Duplicado"));

        LibroException exception = assertThrows(LibroException.class, () -> libroService.guardarLibro(unLibro));

        assertEquals(errorEsperado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarLibro() {
        int indiceLista = 0;
        LibroDTO unLibro = librosDto.get(indiceLista);

        when(libroRepository.findById(unLibro.getId())).thenReturn(Optional.of(librosDao.get(0)));
        when(libroRepository.save(any(LibroDAO.class))).thenReturn(librosDao.get(indiceLista));

        LibroDTO resultado = libroService.actualizarLibro(unLibro);

        assertEquals(unLibro.getTitulo(), resultado.getTitulo());
    }

    @Test
    void testActualizarLibroNotFound() {
        LibroDTO unLibro = librosDto.get(0);

        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        LibroException exception = assertThrows(LibroException.class, () -> libroService.actualizarLibro(unLibro));

        assertEquals(errorLibroNoEncontrado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarLibroISBNDuplicado() {
        LibroDTO unLibro = librosDto.get(0);

        when(libroRepository.findById(unLibro.getId())).thenReturn(Optional.of(librosDao.get(0)));
        when(libroRepository.save(any(LibroDAO.class)))
                .thenThrow(new DataIntegrityViolationException("ISBN Duplicado"));

        LibroException exception = assertThrows(LibroException.class, () -> libroService.actualizarLibro(unLibro));

        assertEquals(errorISBNDuplicado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarParcialmenteLibro() {
        int indiceLista = 0;
        LibroDTO unLibro = librosDto.get(indiceLista);
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("titulo", "Nuevo Título");
        nuevosDatos.put("isbn", "nuevo ISBN");
        nuevosDatos.put("fechaPublicacion", LocalDate.now().toString());

        when(libroRepository.findById(unLibro.getId())).thenReturn(Optional.of(librosDao.get(0)));
        when(libroRepository.save(any(LibroDAO.class))).thenReturn(librosDao.get(indiceLista));

        LibroDTO resultado = libroService.actualizarParcialmenteLibro(unLibro.getId(), nuevosDatos);

        assertEquals(nuevosDatos.get("titulo"), resultado.getTitulo());
        assertEquals(nuevosDatos.get("isbn"), resultado.getIsbn());
        assertEquals(nuevosDatos.get("fechaPublicacion"), resultado.getFechaPublicacion().toString());
    }

    @Test
    void testActualizarParcialmenteLibroNotFound() {
        LibroDTO unLibro = librosDto.get(0);
        Long libroId = unLibro.getId();
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("titulo", "Nuevo Título");

        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        LibroException exception = assertThrows(
                LibroException.class,
                () -> libroService.actualizarParcialmenteLibro(libroId, nuevosDatos)
        );

        assertEquals(errorLibroNoEncontrado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarParcialmenteLibroISBNDuplicado() {
        LibroDTO unLibro = librosDto.get(0);
        Long libroId = unLibro.getId();
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("autor", "Nuevo Autor");

        when(libroRepository.findById(unLibro.getId())).thenReturn(Optional.of(librosDao.get(0)));
        when(libroRepository.save(any(LibroDAO.class)))
                .thenThrow(new DataIntegrityViolationException("ISBN Duplicado"));

        LibroException exception = assertThrows(
                LibroException.class,
                () -> libroService.actualizarParcialmenteLibro(libroId, nuevosDatos)
        );

        assertEquals(errorISBNDuplicado.getMessage(), exception.getMessage());
    }

    @Test
    void testBorrarLibro() {
        Long libroId = 1L;
        LibroDTO unLibroDto = librosDto.get(0);
        LibroDAO unLibroDao = librosDao.get(0);

        when(libroRepository.findById(unLibroDto.getId())).thenReturn(Optional.of(unLibroDao));
        doNothing().when(libroRepository).delete(unLibroDao);

        libroService.borrarLibro(libroId);

        verify(libroRepository, times(1)).findById(libroId);
        verify(libroRepository, times(1)).delete(unLibroDao);
    }

    @Test
    void testBorrarLibroNotFound() {
        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        LibroException exception = assertThrows(LibroException.class, () -> libroService.borrarLibro(1L));

        assertEquals(errorLibroNoEncontrado.getMessage(), exception.getMessage());
    }
}