package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dao.PrestamoDAO;
import com.example.ejercicio_java.dao.UsuarioDAO;
import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.dto.PrestamoDTO;
import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.exceptions.prestamo.PrestamoException;
import com.example.ejercicio_java.exceptions.usuario.UsuarioException;
import com.example.ejercicio_java.repository.LibroRepository;
import com.example.ejercicio_java.repository.PrestamoRepository;
import com.example.ejercicio_java.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.ejercicio_java.service.impl.LibroServiceImpl.LIBRO_NO_ENCONTRADO_MENSAJE;
import static com.example.ejercicio_java.service.impl.PrestamoServiceImpl.PRESTAMO_NO_ENCONTRADO_MENSAJE;
import static com.example.ejercicio_java.service.impl.UsuarioServiceImpl.USUARIO_NO_ENCONTRADO_MENSAJE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PrestamoServiceImplTest {

    private static final Long ID_NO_VALIDA = 0L;

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    private PrestamoException prestamoExceptionNoEncontrado;

    private final List<PrestamoDTO> prestamoDTOList = new ArrayList<>();
    private final List<PrestamoDAO> prestamoDAOList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        int veces = 3;
        //Generar préstamos DTO
        for (int i = 1; i <= veces; i++) {
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
                    LocalDate.now().minusWeeks(i),
                    LocalDate.now()
            );
            prestamoDTOList.add(prestamoDTO);
        }
        //Generar préstamos DAO
        for (int i = 1; i <= veces; i++) {
            PrestamoDAO prestamoDAO = new PrestamoDAO(
                    new UsuarioDAO(
                            "Usuario " + i,
                            "email_" + i + "@mail.mail",
                            String.format("700%07d", i),
                            LocalDate.now().minusDays(i)
                    ),
                    new LibroDAO(
                            "Libro " + i,
                            "Autor " + i,
                            "isbn " + i,
                            LocalDate.now().minusYears(i)
                    ),
                    LocalDate.now().minusWeeks(i),
                    LocalDate.now()
            );
            prestamoDAO.getUsuario().setId((long) i);
            prestamoDAO.getLibro().setId((long) i);
            prestamoDAO.setId((long) i);
            prestamoDAOList.add(prestamoDAO);
        }
        prestamoExceptionNoEncontrado = new PrestamoException(
                PrestamoException.NO_ENCONTRADO,
                String.format(PRESTAMO_NO_ENCONTRADO_MENSAJE, ID_NO_VALIDA)
        );
    }

    @Test
    void testOtenerPrestamos() {
        when(prestamoRepository.findAll()).thenReturn(prestamoDAOList);

        List<PrestamoDTO> resultado = prestamoService.obtenerPrestamos();

        assertEquals(3, resultado.size());
    }

    @Test
    void testObtenerUnPrestamo() {
        PrestamoDAO unPrestamo = prestamoDAOList.get(0);

        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.of(unPrestamo));

        PrestamoDTO resultado = prestamoService.obtenerUnPrestamo(unPrestamo.getId());

        assertEquals(unPrestamo.getFechaPrestamo(), resultado.getFechaPrestamo());
    }

    @Test
    void testObtenerUnPrestamoNotFound() {
        PrestamoException prestamoException = assertThrows(
                PrestamoException.class, () -> prestamoService.obtenerUnPrestamo(ID_NO_VALIDA));

        assertEquals(prestamoExceptionNoEncontrado.getMessage(), prestamoException.getMessage());
    }

    @Test
    void testGuardarPrestamo() {
        PrestamoDAO prestamoDAO = prestamoDAOList.get(0);
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);

        when(prestamoRepository.save(any(PrestamoDAO.class))).thenReturn(prestamoDAO);

        PrestamoDTO resultado = prestamoService.guardarPrestamo(prestamoDTO);

        assertEquals(prestamoDTO.getFechaPrestamo(), resultado.getFechaPrestamo());
    }

    @Test
    void testActualizarPrestamo() {
        PrestamoDAO prestamoDAO = prestamoDAOList.get(0);
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);

        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO));
        when(prestamoRepository.save(any(PrestamoDAO.class))).thenReturn(prestamoDAO);

        PrestamoDTO resultado = prestamoService.actualizarPrestamo(prestamoDTO);

        assertEquals(prestamoDTO.getId(), resultado.getId());
        assertEquals(prestamoDTO.getUsuario().getId(), resultado.getUsuario().getId());
        assertEquals(prestamoDTO.getUsuario().getNombre(), resultado.getUsuario().getNombre());
        assertEquals(prestamoDTO.getUsuario().getEmail(), resultado.getUsuario().getEmail());
        assertEquals(prestamoDTO.getUsuario().getTelefono(), resultado.getUsuario().getTelefono());
        assertEquals(prestamoDTO.getUsuario().getFechaRegistro(), resultado.getUsuario().getFechaRegistro());
        assertEquals(prestamoDTO.getLibro().getId(), resultado.getLibro().getId());
        assertEquals(prestamoDTO.getLibro().getTitulo(), resultado.getLibro().getTitulo());
        assertEquals(prestamoDTO.getLibro().getAutor(), resultado.getLibro().getAutor());
        assertEquals(prestamoDTO.getLibro().getIsbn(), resultado.getLibro().getIsbn());
        assertEquals(prestamoDTO.getLibro().getFechaPublicacion(), resultado.getLibro().getFechaPublicacion());
        assertEquals(prestamoDTO.getFechaPrestamo(), resultado.getFechaPrestamo());
        assertEquals(prestamoDTO.getFechaDevolucion(), resultado.getFechaDevolucion());
    }

    @Test
    void testActualizarPrestamoNotFound() {
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);
        prestamoDTO.setId(ID_NO_VALIDA);

        PrestamoException prestamoException = assertThrows(
                PrestamoException.class, () -> prestamoService.actualizarPrestamo(prestamoDTO));

        assertEquals(prestamoExceptionNoEncontrado.getMessage(), prestamoException.getMessage());
    }

    @Test
    void testActualizarParcialmentePrestamo() {
        PrestamoDAO prestamoDAO = prestamoDAOList.get(0);
        Map<String, Object> update = new HashMap<>();
        update.put("usuarioId", prestamoDAO.getUsuario().getId());
        update.put("libroId", prestamoDAO.getLibro().getId());
        update.put("fechaPrestamo", LocalDate.now().toString());
        update.put("fechaDevolucion", LocalDate.now().toString());

        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO));
        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO.getUsuario()));
        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO.getLibro()));
        when(prestamoRepository.save(any(PrestamoDAO.class))).thenReturn(prestamoDAO);

        PrestamoDTO resultado = prestamoService.actualizarParcialmentePrestamo(prestamoDAO.getId(), update);

        assertEquals(prestamoDAO.getId(), resultado.getId());
        assertEquals(prestamoDAO.getUsuario().getId(), resultado.getUsuario().getId());
        assertEquals(prestamoDAO.getLibro().getId(), resultado.getLibro().getId());
        assertEquals(prestamoDAO.getFechaPrestamo(), resultado.getFechaPrestamo());
        assertEquals(prestamoDAO.getFechaDevolucion(), resultado.getFechaDevolucion());
    }

    @Test
    void testActualizarParcialmentePrestamoUsuarioNotFound() {
        UsuarioException usuarioException = new UsuarioException(
                UsuarioException.NO_ENCONTRADO, String.format(USUARIO_NO_ENCONTRADO_MENSAJE, ID_NO_VALIDA));
        PrestamoDAO prestamoDAO = prestamoDAOList.get(0);
        Long prestamoId = prestamoDAO.getId();
        Map<String, Object> update = new HashMap<>();
        update.put("usuarioId", ID_NO_VALIDA);
        update.put("libroId", prestamoDAO.getLibro().getId());
        update.put("fechaPrestamo", LocalDate.now().toString());
        update.put("fechaDevolucion", LocalDate.now().toString());

        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO));

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> prestamoService.actualizarParcialmentePrestamo(prestamoId, update)
        );

        assertEquals(usuarioException.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarParcialmentePrestamoLibroNotFound() {
        LibroException libroException = new LibroException(
                LibroException.NO_ENCONTRADO, String.format(LIBRO_NO_ENCONTRADO_MENSAJE, ID_NO_VALIDA));
        PrestamoDAO prestamoDAO = prestamoDAOList.get(0);
        Long prestamoId = prestamoDAO.getId();
        Map<String, Object> update = new HashMap<>();
        update.put("usuarioId", prestamoDAO.getUsuario().getId());
        update.put("libroId", ID_NO_VALIDA);
        update.put("fechaPrestamo", LocalDate.now().toString());
        update.put("fechaDevolucion", LocalDate.now().toString());

        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO));
        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO.getUsuario()));

        LibroException exception = assertThrows(
                LibroException.class,
                () -> prestamoService.actualizarParcialmentePrestamo(prestamoId, update)
        );

        assertEquals(libroException.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarParcialmentePrestamoNotFound() {
        Map<String, Object> update = new HashMap<>();

        PrestamoException exception = assertThrows(
                PrestamoException.class,
                () -> prestamoService.actualizarParcialmentePrestamo(ID_NO_VALIDA, update)
        );

        assertEquals(prestamoExceptionNoEncontrado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarParcialmentePrestamo_SinUsuarioSinLibroConFechaPrestamoSinFechaDevolucion() {
        PrestamoDAO prestamoDAO = prestamoDAOList.get(0);
        Map<String, Object> update = new HashMap<>();
        update.put("fechaPrestamo", LocalDate.now().minusWeeks(1).toString());
        prestamoDAO.setFechaPrestamo(LocalDate.now().minusWeeks(1));

        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO));
        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO.getUsuario()));
        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO.getLibro()));
        when(prestamoRepository.save(any(PrestamoDAO.class))).thenReturn(prestamoDAO);

        PrestamoDTO resultado = prestamoService.actualizarParcialmentePrestamo(prestamoDAO.getId(), update);

        assertEquals(prestamoDAO.getId(), resultado.getId());
        assertEquals(prestamoDAO.getUsuario().getId(), resultado.getUsuario().getId());
        assertEquals(prestamoDAO.getLibro().getId(), resultado.getLibro().getId());
        assertEquals(prestamoDAO.getFechaPrestamo(), resultado.getFechaPrestamo());
        assertEquals(prestamoDAO.getFechaDevolucion(), resultado.getFechaDevolucion());
    }

    @Test
    void testActualizarParcialmentePrestamo_SinUsuarioSinLibroSinFechaPrestamoConFechaDevolucion() {
        PrestamoDAO prestamoDAO = prestamoDAOList.get(0);
        Map<String, Object> update = new HashMap<>();
        update.put("fechaDevolucion", LocalDate.now().minusWeeks(1).toString());
        prestamoDAO.setFechaDevolucion(LocalDate.now().minusWeeks(1));

        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO));
        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO.getUsuario()));
        when(libroRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO.getLibro()));
        when(prestamoRepository.save(any(PrestamoDAO.class))).thenReturn(prestamoDAO);

        PrestamoDTO resultado = prestamoService.actualizarParcialmentePrestamo(prestamoDAO.getId(), update);

        assertEquals(prestamoDAO.getId(), resultado.getId());
        assertEquals(prestamoDAO.getUsuario().getId(), resultado.getUsuario().getId());
        assertEquals(prestamoDAO.getLibro().getId(), resultado.getLibro().getId());
        assertEquals(prestamoDAO.getFechaPrestamo(), resultado.getFechaPrestamo());
        assertEquals(prestamoDAO.getFechaDevolucion(), resultado.getFechaDevolucion());
    }

    @Test
    void testBorrarPrestamo() {
        PrestamoDAO prestamoDAO = prestamoDAOList.get(0);
        PrestamoDTO prestamoDTO = prestamoDTOList.get(0);

        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.of(prestamoDAO));
        doNothing().when(prestamoRepository).delete(prestamoDAO);

        prestamoService.borrarPrestamo(prestamoDTO.getId());

        verify(prestamoRepository, times(1)).findById(prestamoDTO.getId());
        verify(prestamoRepository, times(1)).delete(prestamoDAO);
    }

    @Test
    void testBorrarPrestamoNotFound() {

        PrestamoException exception = assertThrows(
                PrestamoException.class,
                () -> prestamoService.borrarPrestamo(ID_NO_VALIDA)
        );

        assertEquals(prestamoExceptionNoEncontrado.getMessage(), exception.getMessage());
    }
}