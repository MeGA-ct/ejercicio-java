package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.exceptions.LibroNotFoundException;
import com.example.ejercicio_java.mapper.LibroMapper;
import com.example.ejercicio_java.repository.LibroRepository;
import com.example.ejercicio_java.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServiceImpl implements LibroService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LibroServiceImpl.class);

    private static final LibroMapper libroMapper = LibroMapper.INSTANCE;

    private final LibroRepository libroRepository;
    @Autowired
    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public List<LibroDTO> obtenerLibros() {
        LOGGER.debug("LibroServiceImpl.obtenerLibros: obteniendo todos los libros ... ");

        List<LibroDAO> libros = libroRepository.findAll();

        List<LibroDTO> resultado = libroMapper.librosDaoToLibrosDto(libros);

        LOGGER.debug("LibroServiceImpl.obtenerLibros: se han obtenido {} libros.", resultado.size());
        return resultado;
    }

    @Override
    public LibroDTO obtenerUnLibro(final Long libroId) {
        LOGGER.debug("LibroServiceImpl.obtenerUnLibro: obteniendo todos los libros ... ");

        LibroDAO libro = libroRepository.findById(libroId)
                                        .orElseThrow(() -> new LibroNotFoundException(libroId));

        LibroDTO resultado = libroMapper.libroDaoToLibroDto(libro);

        LOGGER.debug("LibroServiceImpl.obtenerUnLibro: libro {} por {} obtenido.",
                     resultado.getTitulo(), resultado.getAutor());
        return resultado;
    }
}
