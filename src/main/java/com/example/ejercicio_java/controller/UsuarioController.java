package com.example.ejercicio_java.controller;

import com.example.ejercicio_java.controller.request.UsuarioBody;
import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.usuario.UsuarioException;
import com.example.ejercicio_java.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public static final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuarios() {

        LOGGER.info("UsuarioController.obtenerUsuarios: obteniendo todos los usuarios");

        List<UsuarioDTO> resultado = usuarioService.obtenerUsuarios();

        LOGGER.info("UsuarioController.obtenerUsuarios: se han obtenido {} usuarios.", resultado.size());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> obtenerUnUsuario(@PathVariable("id") Long libroId) {
        LOGGER.info("UsuarioController.obtenerUnUsuario: obteniendo un usuario");

        try {
            UsuarioDTO resultado = usuarioService.obtenerUnUsuario(libroId);

            LOGGER.info("UsuarioController.obtenerUnUsuario: usuario {} encontrado", resultado.getEmail());

            return ResponseEntity.ok(resultado);
        } catch (UsuarioException e) {
            LOGGER.info("UsuarioController.obtenerUnUsuario: error al obtener usuario con id {}.", libroId);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> guardarUsuario(@RequestBody UsuarioBody libro) {
        LOGGER.info("UsuarioController.guardarUsuario: guardando un nuevo libro");

        UsuarioDTO libroDTO = new UsuarioDTO(
                null,
                libro.nombre(),
                libro.email(),
                libro.telefono(),
                libro.fechaRegistro()
        );

        try {
            UsuarioDTO resultado = usuarioService.guardarUsuario(libroDTO);

            LOGGER.info("UsuarioController.guardarUsuario: creado el usuario con id {}", resultado.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (UsuarioException e) {
            LOGGER.info("UsuarioController.guardarUsuario: error al crear usuario");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> actualizarUsuario(
            @PathVariable("id") Long usuarioId,
            @RequestBody UsuarioBody libro
    ) {
        LOGGER.info("UsuarioController.actualizarUsuario: actualizando usuario {}", usuarioId);
        UsuarioDTO libroDTO = new UsuarioDTO(
                usuarioId,
                libro.nombre(),
                libro.email(),
                libro.telefono(),
                libro.fechaRegistro()
        );
        try {
            UsuarioDTO resultado = usuarioService.actualizarUsuario(libroDTO);

            LOGGER.info("UsuarioController.actualizarUsuario: modificado el usuario con id {}", resultado.getId());
            return ResponseEntity.ok(resultado);
        } catch (UsuarioException e) {
            LOGGER.info("UsuarioController.actualizarUsuario: error al modificar libro con id {}", usuarioId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> actualizarParcialmenteUsuario(
            @PathVariable("id") Long usuarioId,
            @RequestBody Map<String, Object> updates
    ) {
        LOGGER.info(
                "UsuarioController.actualizarParcialmenteUsuario: actualización parcial del usuario {}",
                usuarioId
        );
        try {
            UsuarioDTO resultado = usuarioService.actualizarParcialmenteUsuario(usuarioId, updates);
            LOGGER.info(
                    "UsuarioController.actualizarParcialmenteUsuario: actualizado parcialmente el usuario {}",
                    resultado.getId()
            );
            return ResponseEntity.ok(resultado);
        } catch (UsuarioException e) {
            LOGGER.info("UsuarioController.actualizarParcialmenteUsuario: error al modificar usuario {}", usuarioId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> borrarUsuario(
            @PathVariable("id") Long usuarioId
    ) {
        LOGGER.info("UsuarioController.borrarUsuario: borrando el usuario con id {}", usuarioId);
        try {
            usuarioService.borrarUsuario(usuarioId);
            LOGGER.info("UsuarioController.borrarUsuario: se ha borrado usuario con id {}", usuarioId);
            return ResponseEntity.noContent().build();
        } catch (UsuarioException e) {
            LOGGER.info("UsuarioController.borrarUsuario: error al borrar usuario con id {}", usuarioId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
