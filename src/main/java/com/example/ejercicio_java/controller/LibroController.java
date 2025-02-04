package com.example.ejercicio_java.controller;

import com.example.ejercicio_java.controller.request.LibroBody;
import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(path = "/libros")
public class LibroController {

    private final LibroService libroService;

    public static final Logger LOGGER = LoggerFactory.getLogger(LibroController.class);

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<LibroDTO>> obtenerLibros() {

        LOGGER.debug("LibroController.obtenerLibros: obteniendo todos los libros ... ");

        List<LibroDTO> resultado = libroService.obtenerLibros();

        LOGGER.debug("LibroController.obtenerLibros: se han obtenido {} libros.", resultado.size());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> obtenerUnLibro(@PathVariable("id") Long libroId) {
        LOGGER.debug("LibroController.obtenerUnLibro: obteniendo todos los libros ... ");

        try {
            LibroDTO resultado = libroService.obtenerUnLibro(libroId);
            LOGGER.debug(
                    "LibroController.obtenerUnLibro: se ha obtenido el libro {} del autor {}.",
                    resultado.getTitulo(),
                    resultado.getAutor()
            );
            return ResponseEntity.ok(resultado);
        } catch (LibroException e){
            LOGGER.debug("LibroController.obtenerUnLibro: error al obtener libro con id {}.", libroId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<LibroDTO> guardarLibro(@RequestBody LibroBody libro) {
        LOGGER.debug("LibroController.guardarLibro: guardando un nuevo libro ... ");

        LibroDTO libroDTO = new LibroDTO(
                null,
                libro.titulo(),
                libro.autor(),
                libro.isbn(),
                libro.fechaPublicacion()
        );

        LibroDTO resultado = libroService.guardarLibro(libroDTO);

        LOGGER.debug(
                "LibroController.guardarLibro: se ha guardado el libro {} del autor {}.",
                resultado.getTitulo(),
                resultado.getAutor()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> actualizarLibro(
            @PathVariable("id") Long libroId,
            @RequestBody LibroBody libro
    ) {
        LOGGER.debug("LibroController.actualizarLibro: actualizando el libro con id {} ... ", libroId);
        LibroDTO libroDTO = new LibroDTO(
                libroId,
                libro.titulo(),
                libro.autor(),
                libro.isbn(),
                libro.fechaPublicacion()
        );
        try {
            LibroDTO resultado = libroService.actualizarLibro(libroDTO);
            LOGGER.debug(
                    "LibroController.actualizarLibro: se ha actualizado el libro {} del autor {}.",
                    resultado.getTitulo(),
                    resultado.getAutor()
            );
            return ResponseEntity.ok(resultado);
        } catch (LibroException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<LibroDTO> actualizarParcialmenteLibro(
            @PathVariable("id") Long libroId,
            @RequestBody Map<String, Object> updates
    ) {
        LOGGER.debug(
                "LibroController.actualizarParcialmenteLibro: actualizando parcialmente el libro con id {} ... ",
                libroId
        );

        LibroDTO resultado = libroService.actualizarParcialmenteLibro(libroId, updates);

        LOGGER.debug(
                "LibroController.actualizarParcialmenteLibro: se ha actualizado parcialmente el libro {} del autor {}.",
                resultado.getTitulo(),
                resultado.getAutor()
        );
        return ResponseEntity.ok(resultado);
    }

}
