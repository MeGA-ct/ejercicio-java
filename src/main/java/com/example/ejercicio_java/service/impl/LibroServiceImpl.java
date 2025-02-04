package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.exceptions.DuplicateIsbnException;
import com.example.ejercicio_java.exceptions.LibroNotFoundException;
import com.example.ejercicio_java.mapper.LibroMapper;
import com.example.ejercicio_java.repository.LibroRepository;
import com.example.ejercicio_java.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        LOGGER.debug("LibroServiceImpl.obtenerUnLibro: obteniendo un solo libro ... ");

        LibroDAO libro = libroRepository.findById(libroId).orElseThrow(() -> new LibroNotFoundException(libroId));

        LibroDTO resultado = libroMapper.libroDaoToLibroDto(libro);

        LOGGER.debug(
                "LibroServiceImpl.obtenerUnLibro: libro {} por {} obtenido.",
                resultado.getTitulo(),
                resultado.getAutor()
        );
        return resultado;
    }

    @Override
    public LibroDTO guardarLibro(LibroDTO libroDTO) {
        LOGGER.debug("LibroServiceImpl.guardarLibro: guardando libro ... ");

        LibroDAO libro = libroRepository.save(libroMapper.libroDtoToLibroDao(libroDTO));

        LibroDTO resultado = libroMapper.libroDaoToLibroDto(libro);

        LOGGER.debug(
                "LibroServiceImpl.guardarLibro: libro {} por {} obtenido.",
                resultado.getTitulo(),
                resultado.getAutor()
        );
        return resultado;
    }

    @Override
    public LibroDTO actualizarLibro(LibroDTO libroDTO) {
        LOGGER.debug("LibroServiceImpl.actualizarLibro: guardando libro ... ");
        LibroDAO libro = libroRepository.findById(libroDTO.getId())
                                        .orElseThrow(() -> new LibroNotFoundException(libroDTO.getId()));

        Optional<LibroDAO> existingLibro = libroRepository.findByIsbn(libroDTO.getIsbn());
        if (existingLibro.isPresent() && !existingLibro.get().getId().equals(libro.getId())) {
            throw new DuplicateIsbnException(libroDTO.getIsbn());
        }

        libro.setTitulo(libroDTO.getTitulo());
        libro.setAutor(libroDTO.getAutor());
        libro.setIsbn(libroDTO.getIsbn());
        libro.setFechaPublicacion(libroDTO.getFechaPublicacion());

        LibroDAO libroSaved = libroRepository.save(libro);

        LibroDTO resultado = libroMapper.libroDaoToLibroDto(libroSaved);

        LOGGER.debug(
                "LibroServiceImpl.actualizarLibro: libro {} por {} obtenido.",
                resultado.getTitulo(),
                resultado.getAutor()
        );
        return resultado;
    }

    @Override
    public LibroDTO actualizarParcialmenteLibro(Long libroId, Map<String, Object> updates) {
        LOGGER.debug("LibroServiceImpl.actualizarParcialmenteLibro: guardando libro ... ");

        LibroDAO libro = libroRepository.findById(libroId).orElseThrow(() -> new LibroNotFoundException(libroId));
        if (updates.containsKey("titulo")){
            libro.setTitulo(updates.get("titulo").toString());
        }
        if (updates.containsKey("autor")){
            libro.setAutor(updates.get("autor").toString());
        }
        if (updates.containsKey("isbn")){
            libro.setIsbn(updates.get("isbn").toString());
        }
        if (updates.containsKey("fechaPublicacion")){
            libro.setFechaPublicacion(LocalDate.parse(updates.get("fechaPublicacion").toString()));
        }

        LibroDAO libroReturn = libroRepository.save(libro);

        LibroDTO resultado = libroMapper.libroDaoToLibroDto(libroReturn);

        LOGGER.debug(
                "LibroServiceImpl.actualizarParcialmenteLibro: libro {} por {} obtenido.",
                resultado.getTitulo(),
                resultado.getAutor()
        );
        return resultado;
    }
}
