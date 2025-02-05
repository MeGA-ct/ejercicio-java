package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.UsuarioDAO;
import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.usuario.UsuarioException;
import com.example.ejercicio_java.repository.UsuarioRepository;
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

import static com.example.ejercicio_java.service.impl.UsuarioServiceImpl.USUARIO_EMAIL_DUPLICADO_MENSAJE;
import static com.example.ejercicio_java.service.impl.UsuarioServiceImpl.USUARIO_NO_ENCONTRADO_MENSAJE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    List<UsuarioDAO> usuariosDao = new ArrayList<>();

    List<UsuarioDTO> usuariosDto = new ArrayList<>();

    UsuarioException errorUsuarioNoEncontrado;
    UsuarioException errorISBNDuplicado;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        for (int i = 1; i <= 3; i++) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(
                    "Usuario " + i,
                    "Autor " + i,
                    "isbn " + i,
                    LocalDate.now().minusDays(i)
            );
            usuarioDAO.setId((long) i);
            usuariosDao.add(usuarioDAO);

            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    (long) i,
                    "Usuario " + i,
                    "Autor " + i,
                    "isbn " + i,
                    LocalDate.now().minusDays(i)
            );
            usuariosDto.add(usuarioDTO);
        }
        errorUsuarioNoEncontrado = new UsuarioException(
                UsuarioException.NO_ENCONTRADO,
                String.format(
                        USUARIO_NO_ENCONTRADO_MENSAJE,
                        usuariosDto.get(0).getId()
                )
        );
        errorISBNDuplicado = new UsuarioException(
                UsuarioException.EMAIL_DUPLICADO,
                String.format(
                        USUARIO_EMAIL_DUPLICADO_MENSAJE,
                        usuariosDto.get(0).getEmail()
                )
        );
    }

    @Test
    void testObtenerUsuarios() {

        when(usuarioRepository.findAll()).thenReturn(usuariosDao);

        List<UsuarioDTO> resultado = usuarioService.obtenerUsuarios();

        assertEquals(3, resultado.size());
    }

    @Test
    void testObtenerUnUsuario() {
        int indiceLista = 0;
        UsuarioDAO unUsuario = usuariosDao.get(indiceLista);
        unUsuario.setId((long) indiceLista); //Guardado simulado

        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(unUsuario));

        UsuarioDTO resultado = usuarioService.obtenerUnUsuario(unUsuario.getId());

        assertEquals(unUsuario.getNombre(), resultado.getNombre());
    }

    @Test
    void testObtenerUnUsuarioNotFound() {
        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        UsuarioException exception = assertThrows(UsuarioException.class, () -> usuarioService.obtenerUnUsuario(1L));

        assertEquals(errorUsuarioNoEncontrado.getMessage(), exception.getMessage());
    }

    @Test
    void testGuardarUsuario() {
        int indiceLista = 0;
        UsuarioDTO unUsuario = usuariosDto.get(indiceLista);

        when(usuarioRepository.save(any(UsuarioDAO.class))).thenReturn(usuariosDao.get(indiceLista));

        UsuarioDTO resultado = usuarioService.guardarUsuario(unUsuario);

        assertEquals(unUsuario.getEmail(), resultado.getEmail());
    }

    @Test
    void testGuardarUsuarioISBNDuplicado() {
        UsuarioDTO unUsuario = usuariosDto.get(0);
        UsuarioException errorEsperado = new UsuarioException(
                UsuarioException.EMAIL_DUPLICADO,
                String.format(
                        USUARIO_EMAIL_DUPLICADO_MENSAJE,
                        usuariosDto.get(0).getEmail()
                )
        );

        when(usuarioRepository.save(any(UsuarioDAO.class))).thenThrow(new DataIntegrityViolationException(
                "ISBN Duplicado"));

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.guardarUsuario(unUsuario)
        );

        assertEquals(errorEsperado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarUsuario() {
        int indiceLista = 0;
        UsuarioDTO unUsuario = usuariosDto.get(indiceLista);

        when(usuarioRepository.findById(unUsuario.getId())).thenReturn(Optional.of(usuariosDao.get(0)));
        when(usuarioRepository.save(any(UsuarioDAO.class))).thenReturn(usuariosDao.get(indiceLista));

        UsuarioDTO resultado = usuarioService.actualizarUsuario(unUsuario);

        assertEquals(unUsuario.getId(), resultado.getId());
        assertEquals(unUsuario.getNombre(), resultado.getNombre());
        assertEquals(unUsuario.getEmail(), resultado.getEmail());
        assertEquals(unUsuario.getTelefono(), resultado.getTelefono());
        assertEquals(unUsuario.getFechaRegistro(), resultado.getFechaRegistro());
    }

    @Test
    void testActualizarUsuarioNotFound() {
        UsuarioDTO unUsuario = usuariosDto.get(0);

        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.actualizarUsuario(unUsuario)
        );

        assertEquals(errorUsuarioNoEncontrado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarUsuarioISBNDuplicado() {
        UsuarioDTO unUsuario = usuariosDto.get(0);

        when(usuarioRepository.findById(unUsuario.getId())).thenReturn(Optional.of(usuariosDao.get(0)));
        when(usuarioRepository.save(any(UsuarioDAO.class))).thenThrow(new DataIntegrityViolationException(
                "ISBN Duplicado"));

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.actualizarUsuario(unUsuario)
        );

        assertEquals(errorISBNDuplicado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarParcialmenteUsuario() {
        int indiceLista = 0;
        UsuarioDTO unUsuario = usuariosDto.get(indiceLista);
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Nuevo Nombre");
        nuevosDatos.put("telefono", "Nuevo Telef.");
        nuevosDatos.put("fechaRegistro", LocalDate.now().toString());

        when(usuarioRepository.findById(unUsuario.getId())).thenReturn(Optional.of(usuariosDao.get(0)));
        when(usuarioRepository.save(any(UsuarioDAO.class))).thenReturn(usuariosDao.get(indiceLista));

        UsuarioDTO resultado = usuarioService.actualizarParcialmenteUsuario(unUsuario.getId(), nuevosDatos);

        assertEquals(nuevosDatos.get("nombre"), resultado.getNombre());
        assertEquals(nuevosDatos.get("telefono"), resultado.getTelefono());
        assertEquals(nuevosDatos.get("fechaRegistro"), resultado.getFechaRegistro().toString());
    }

    @Test
    void testActualizarParcialmenteUsuarioNotFound() {
        UsuarioDTO unUsuario = usuariosDto.get(0);
        Long usuarioId = unUsuario.getId();
        Map<String, Object> nuevosDatos = new HashMap<>();

        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.actualizarParcialmenteUsuario(
                        usuarioId,
                        nuevosDatos
                )
        );

        assertEquals(errorUsuarioNoEncontrado.getMessage(), exception.getMessage());
    }

    @Test
    void testActualizarParcialmenteUsuarioISBNDuplicado() {
        UsuarioDTO unUsuario = usuariosDto.get(0);
        Long usuarioId = unUsuario.getId();
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("email", unUsuario.getEmail());

        when(usuarioRepository.findById(unUsuario.getId())).thenReturn(Optional.of(usuariosDao.get(0)));
        when(usuarioRepository.save(any(UsuarioDAO.class))).thenThrow(new DataIntegrityViolationException(
                "EMAIL Duplicado"));

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.actualizarParcialmenteUsuario(
                        usuarioId,
                        nuevosDatos
                )
        );

        assertEquals(errorISBNDuplicado.getMessage(), exception.getMessage());
    }

    @Test
    void testBorrarUsuario() {
        Long usuarioId = 1L;
        UsuarioDTO unUsuarioDto = usuariosDto.get(0);
        UsuarioDAO unUsuarioDao = usuariosDao.get(0);

        when(usuarioRepository.findById(unUsuarioDto.getId())).thenReturn(Optional.of(unUsuarioDao));
        doNothing().when(usuarioRepository).delete(unUsuarioDao);

        usuarioService.borrarUsuario(usuarioId);

        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).delete(unUsuarioDao);
    }

    @Test
    void testBorrarUsuarioNotFound() {
        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.borrarUsuario(1L)
        );

        assertEquals(errorUsuarioNoEncontrado.getMessage(), exception.getMessage());
    }
}