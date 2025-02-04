package com.example.ejercicio_java.controller;

import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.usuario.UsuarioException;
import com.example.ejercicio_java.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**TODO: @GetMapping(path = "/{id}") 
    public ResponseEntity<Object> obtenerUnLibro(@PathVariable("id") Long libroId) {
        LOGGER.info("UsuarioController.obtenerUnLibro: obteniendo todos los libros");

        try {
            LibroDTO resultado = usuarioService.obtenerUnLibro(libroId);
            LOGGER.info(
                    "UsuarioController.obtenerUnLibro: se ha obtenido el libro {} del autor {}.",
                    resultado.getTitulo(),
                    resultado.getAutor()
            );
            return ResponseEntity.ok(resultado);
        } catch (LibroException e) {
            LOGGER.info("UsuarioController.obtenerUnLibro: error al obtener libro con id {}.", libroId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*@PostMapping
    public ResponseEntity<Object> guardarLibro(@RequestBody LibroBody libro) {
        LOGGER.info("UsuarioController.guardarLibro: guardando un nuevo libro");
        try {
            LibroDTO libroDTO = new LibroDTO(
                    null,
                    libro.titulo(),
                    libro.autor(),
                    libro.isbn(),
                    libro.fechaPublicacion()
            );
            LibroDTO resultado = usuarioService.guardarLibro(libroDTO);
            LOGGER.info(
                    "UsuarioController.guardarLibro: se ha guardado el libro {} del autor {} con id {}",
                    resultado.getTitulo(),
                    resultado.getAutor(),
                    resultado.getId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (LibroException e) {
            LOGGER.info("UsuarioController.guardarLibro: error al guardar libro");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*@PutMapping(path = "/{id}")
    public ResponseEntity<Object> actualizarLibro(
            @PathVariable("id") Long libroId,
            @RequestBody LibroBody libro
    ) {
        LOGGER.info("UsuarioController.actualizarLibro: actualizando el libro con id {}", libroId);
        LibroDTO libroDTO = new LibroDTO(
                libroId,
                libro.titulo(),
                libro.autor(),
                libro.isbn(),
                libro.fechaPublicacion()
        );
        try {
            LibroDTO resultado = usuarioService.actualizarLibro(libroDTO);
            LOGGER.info(
                    "UsuarioController.actualizarLibro: se ha actualizado el libro {} del autor {}.",
                    resultado.getTitulo(),
                    resultado.getAutor()
            );
            return ResponseEntity.ok(resultado);
        } catch (LibroException e) {
            LOGGER.info("UsuarioController.actualizarLibro: error al modificar libro con id {}", libroId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*@PatchMapping(path = "/{id}")
    public ResponseEntity<Object> actualizarParcialmenteLibro(
            @PathVariable("id") Long libroId,
            @RequestBody Map<String, Object> updates
    ) {
        LOGGER.info(
                "UsuarioController.actualizarParcialmenteLibro: actualizando parcialmente el libro con id {}",
                libroId
        );
        try {
            LibroDTO resultado = usuarioService.actualizarParcialmenteLibro(libroId, updates);
            LOGGER.info(
                    "UsuarioController.actualizarParcialmenteLibro: se ha actualizado parcialmente libro con id {}",
                    resultado.getId()
            );
            return ResponseEntity.ok(resultado);
        } catch (LibroException e) {
            LOGGER.info("UsuarioController.actualizarParcialmenteLibro: error al modificar libro con id {}", libroId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }*/

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
