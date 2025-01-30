package com.example.ejercicio_java.controller;

import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/libros")
public class LibroController {

    private final LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(LibroController.class);

    @GetMapping
    public ResponseEntity<List<LibroDTO>> obtenerLibros(){


        LOGGER.debug("LibroController.obtenerLibros: obteniendo todos los libros ... ");

        List<LibroDTO> resultado = libroService.obtenerLibros();

        LOGGER.debug("LibroController.obtenerLibros: se han obtenido {} libros.", resultado.size());

        return ResponseEntity.ok(resultado);
    }
}
