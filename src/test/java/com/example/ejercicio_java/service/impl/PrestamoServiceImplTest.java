package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dao.PrestamoDAO;
import com.example.ejercicio_java.dao.UsuarioDAO;
import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.dto.PrestamoDTO;
import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.prestamo.PrestamoException;
import com.example.ejercicio_java.repository.PrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.ejercicio_java.service.impl.PrestamoServiceImpl.PRESTAMO_NO_ENCONTRADO_MENSAJE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PrestamoServiceImplTest {

    @Mock
    private PrestamoRepository prestamoRepository;

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
                    LocalDate.now().minusDays(7L * i),
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
                    LocalDate.now().minusDays(7L * i),
                    LocalDate.now()
            );
            prestamoDAO.getUsuario().setId((long) i);
            prestamoDAO.getLibro().setId((long) i);
            prestamoDAO.setId((long) i);
            prestamoDAOList.add(prestamoDAO);
        }
        prestamoExceptionNoEncontrado = new PrestamoException(
                PrestamoException.NO_ENCONTRADO,
                String.format(PRESTAMO_NO_ENCONTRADO_MENSAJE, 0L)
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
        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        PrestamoException prestamoException = assertThrows(
                PrestamoException.class, () -> prestamoService.obtenerUnPrestamo(0L));

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
        prestamoDTO.setId(0L);
        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        PrestamoException prestamoException = assertThrows(
                PrestamoException.class, () -> prestamoService.actualizarPrestamo(prestamoDTO));

        assertEquals(prestamoExceptionNoEncontrado.getMessage(), prestamoException.getMessage());
    }
/*TODO:
    @Test
    void testActualizarParcialmentePrestamo() {
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Nuevo Nombre");
        nuevosDatos.put("telefono", "Nuevo Telef.");
        nuevosDatos.put("fechaRegistro", LocalDate.now().toString());
    }
*/

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
        when(prestamoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        PrestamoException exception = assertThrows(
                PrestamoException.class,
                () -> prestamoService.borrarPrestamo(0L)
        );

        assertEquals(prestamoExceptionNoEncontrado.getMessage(), exception.getMessage());
    }
}