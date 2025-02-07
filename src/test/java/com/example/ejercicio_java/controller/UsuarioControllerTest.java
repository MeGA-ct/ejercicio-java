package com.example.ejercicio_java.controller;

import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.usuario.UsuarioException;
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
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvcC;

    @MockBean
    private UsuarioService usuarioService;

    private final List<UsuarioDTO> usuarios = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        for (int i = 1; i <= 2; i++) {
            UsuarioDTO usuario = new UsuarioDTO(
                    (long) i,
                    "Nombre " + i,
                    "email" + i + "@mail.com",
                    "90090090" + i,
                    LocalDate.now().minusDays(i)
            );
            usuarios.add(usuario);
        }
    }

    @Test
    void testObtenerUsuarios() throws Exception {
        when(usuarioService.obtenerUsuarios()).thenReturn(usuarios);

        mockMvcC.perform(get("/usuarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(usuarios.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].nombre", is(usuarios.get(0).getNombre())))
                .andExpect(jsonPath("$[0].email", is(usuarios.get(0).getEmail())))
                .andExpect(jsonPath("$[0].telefono", is(usuarios.get(0).getTelefono())))
                .andExpect(jsonPath("$[1].id", is(usuarios.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].nombre", is(usuarios.get(1).getNombre())))
                .andExpect(jsonPath("$[1].email", is(usuarios.get(1).getEmail())))
                .andExpect(jsonPath("$[1].telefono", is(usuarios.get(1).getTelefono())));

        verify(usuarioService, times(1)).obtenerUsuarios();
    }

    @Test
    void testObtenerUnUsuario() throws Exception {
        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenReturn(usuarios.get(0));
        mockMvcC.perform(get("/usuarios/0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(usuarios.get(0).getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(usuarios.get(0).getNombre())))
                .andExpect(jsonPath("$.email", is(usuarios.get(0).getEmail())))
                .andExpect(jsonPath("$.telefono", is(usuarios.get(0).getTelefono())));
    }

    @Test
    void testObtenerUnUsuarioNotFound() throws Exception {
        when(usuarioService.obtenerUnUsuario(any(Long.class))).thenThrow(UsuarioException.class);
        mockMvcC.perform(get("/usuarios/0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGuardarUsuario() throws Exception {
        when(usuarioService.guardarUsuario(any(UsuarioDTO.class))).thenReturn(usuarios.get(0));
        mockMvcC.perform(
                        post("/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{" +
                                        " \"nombre\":\"" + usuarios.get(0).getNombre() + "\"," +
                                        " \"email\":\"" + usuarios.get(0).getEmail() + "\"," +
                                        " \"telefono\":\"" + usuarios.get(0).getTelefono() + "\"," +
                                        " \"fechaRegistro\":\"" + usuarios.get(0).getFechaRegistro() + "\"" +
                                        "}"
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(usuarios.get(0).getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(usuarios.get(0).getNombre())))
                .andExpect(jsonPath("$.email", is(usuarios.get(0).getEmail())))
                .andExpect(jsonPath("$.telefono", is(usuarios.get(0).getTelefono())));
    }

    @Test
    void testGuardarUsuarioError() throws Exception {
        when(usuarioService.guardarUsuario(any(UsuarioDTO.class))).thenThrow(UsuarioException.class);
        mockMvcC.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarUsuario() throws Exception {
        when(usuarioService.actualizarUsuario(any(UsuarioDTO.class))).thenReturn(usuarios.get(0));
        mockMvcC.perform(
                        put("/usuarios/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{" +
                                        " \"nombre\":\"" + usuarios.get(0).getNombre() + "\"," +
                                        " \"email\":\"" + usuarios.get(0).getEmail() + "\"," +
                                        " \"telefono\":\"" + usuarios.get(0).getTelefono() + "\"," +
                                        " \"fechaRegistro\":\"" + usuarios.get(0).getFechaRegistro() + "\"" +
                                        "}"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(usuarios.get(0).getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(usuarios.get(0).getNombre())))
                .andExpect(jsonPath("$.email", is(usuarios.get(0).getEmail())))
                .andExpect(jsonPath("$.telefono", is(usuarios.get(0).getTelefono())));
    }

    @Test
    void testActualizarUsuarioError() throws Exception {
        when(usuarioService.actualizarUsuario(any(UsuarioDTO.class))).thenThrow(UsuarioException.class);
        mockMvcC.perform(put("/usuarios/9").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarParcialmenteUsuario() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", "Nuevo Título");
        updates.put("email", "Nuevo Autor");

        when(usuarioService.actualizarParcialmenteUsuario(any(Long.class), anyMap())).thenReturn(usuarios.get(0));
        mockMvcC.perform(patch("/usuarios/1")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(new ObjectMapper().writeValueAsString(updates))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(usuarios.get(0).getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(usuarios.get(0).getNombre())))
                .andExpect(jsonPath("$.email", is(usuarios.get(0).getEmail())))
                .andExpect(jsonPath("$.telefono", is(usuarios.get(0).getTelefono())));
    }

    @Test
    void testActualizarParcialmenteUsuarioError() throws Exception {
        when(usuarioService.actualizarParcialmenteUsuario(any(Long.class), anyMap())).thenThrow(UsuarioException.class);
        mockMvcC.perform(patch("/usuarios/9").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBorrarUsuario() throws Exception {
        Long usuarioId = 1L;

        doNothing().when(usuarioService).borrarUsuario(usuarioId);

        mockMvcC.perform(delete("/usuarios/{id}", usuarioId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).borrarUsuario(usuarioId);
    }

    @Test
    void testBorrarUsuarioError() throws Exception {
        Long usuarioId = 9L;
        String mensajeError = "Usuario no encontrado";

        doThrow(new UsuarioException(UsuarioException.NO_ENCONTRADO, mensajeError))
                .when(usuarioService)
                .borrarUsuario(usuarioId);

        mockMvcC.perform(delete("/usuarios/9").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    void testBorrarUsuarioError_TienePrestamo() throws Exception {
        Long usuarioId = usuarios.get(0).getId();
        String mensajeError = "Usuario con prestamo";

        doThrow(new UsuarioException(UsuarioException.TIENE_PRESTAMOS, mensajeError))
                .when(usuarioService)
                .borrarUsuario(usuarioId);

        mockMvcC.perform(delete("/usuarios/"+usuarioId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}

