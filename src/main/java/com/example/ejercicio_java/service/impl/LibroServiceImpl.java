package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dto.LibroDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.mapper.LibroMapper;
import com.example.ejercicio_java.repository.LibroRepository;
import com.example.ejercicio_java.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class LibroServiceImpl implements LibroService {

    private static final String LIBRO_NO_ENCONTRADO_MENSAJE = "El libro con id[%d] no encontrado";
    private static final String LIBRO_ISBN_DUPLICADO_MENSAJE = "ISBN [%S] ya introducido en otro libro";

    public static final Logger LOGGER = LoggerFactory.getLogger(LibroServiceImpl.class);

    private static final LibroMapper libroMapper = LibroMapper.INSTANCE;

    private final LibroRepository libroRepository;

    @Autowired
    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public List<LibroDTO> obtenerLibros() {
        LOGGER.info("LibroServiceImpl.obtenerLibros: obteniendo todos los libros");

        List<LibroDAO> libros = libroRepository.findAll();

        List<LibroDTO> resultado = libroMapper.librosDaoToLibrosDto(libros);

        LOGGER.info("LibroServiceImpl.obtenerLibros: se han obtenido {} libros.", resultado.size());
        return resultado;
    }

    @Override
    public LibroDTO obtenerUnLibro(final Long libroId) {
        LOGGER.info("LibroServiceImpl.obtenerUnLibro: obteniendo un solo libro");

        LibroDAO libro = libroRepository.findById(libroId).orElseThrow(
                () -> new LibroException(500, String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroId))
        );

        LibroDTO resultado = libroMapper.libroDaoToLibroDto(libro);

        LOGGER.info(
                "LibroServiceImpl.obtenerUnLibro: libro {} por {} obtenido",
                resultado.getTitulo(),
                resultado.getAutor()
        );
        return resultado;
    }

    @Override
    public LibroDTO guardarLibro(LibroDTO libroDTO) throws LibroException {
        LOGGER.info("LibroServiceImpl.guardarLibro: guardando libro");

        try {
            LibroDAO libroGuardado = libroRepository.save(libroMapper.libroDtoToLibroDao(libroDTO));
            LibroDTO resultado = libroMapper.libroDaoToLibroDto(libroGuardado);
            LOGGER.info(
                    "LibroServiceImpl.guardarLibro: libro con id {} obtenido",
                    resultado.getId()
            );
            return resultado;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("LibroServiceImpl.guardarLibro: ISBN Duplicado");
            throw new LibroException(501, String.format(LIBRO_ISBN_DUPLICADO_MENSAJE, libroDTO.getIsbn()));
        }
    }

    @Override
    public LibroDTO actualizarLibro(LibroDTO libroDTO) throws LibroException {
        LOGGER.info("LibroServiceImpl.actualizarLibro: actualizando libro");
        LibroDAO libro = libroRepository
                .findById(libroDTO.getId())
                .orElseThrow(
                        () -> new LibroException(500, String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroDTO.getId()))
                );

        libro.setTitulo(libroDTO.getTitulo());
        libro.setAutor(libroDTO.getAutor());
        libro.setIsbn(libroDTO.getIsbn());
        libro.setFechaPublicacion(libroDTO.getFechaPublicacion());

        try {
            LibroDAO libroGuardado = libroRepository.save(libro);
            LibroDTO resultado = libroMapper.libroDaoToLibroDto(libroGuardado);
            LOGGER.info(
                    "LibroServiceImpl.actualizarLibro: libro con id {} obtenido",
                    resultado.getId()
            );
            return resultado;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("LibroServiceImpl.actualizarLibro: ISBN Duplicado");
            throw new LibroException(501, String.format(LIBRO_ISBN_DUPLICADO_MENSAJE, libro.getIsbn()));
        }
    }

    @Override
    public LibroDTO actualizarParcialmenteLibro(Long libroId, Map<String, Object> updates) throws LibroException {
        LOGGER.info("LibroServiceImpl.actualizarParcialmenteLibro: actualizando parcialmente libro");

        LibroDAO libro = libroRepository.findById(libroId).orElseThrow(
                () -> new LibroException(500, String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroId))
        );
        if (updates.containsKey("titulo")) {
            libro.setTitulo(updates.get("titulo").toString());
        }
        if (updates.containsKey("autor")) {
            libro.setAutor(updates.get("autor").toString());
        }
        if (updates.containsKey("isbn")) {
            libro.setIsbn(updates.get("isbn").toString());
        }
        if (updates.containsKey("fechaPublicacion")) {
            libro.setFechaPublicacion(LocalDate.parse(updates.get("fechaPublicacion").toString()));
        }

        try {
            LibroDAO libroSaved = libroRepository.save(libro);
            LibroDTO resultado = libroMapper.libroDaoToLibroDto(libroSaved);
            LOGGER.info(
                    "LibroServiceImpl.actualizarParcialmenteLibro: libro con id {} obtenido",
                    resultado.getId()
            );
            return resultado;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("LibroServiceImpl.actualizarParcialmenteLibro: ISBN Duplicado");
            throw new LibroException(501, String.format(LIBRO_ISBN_DUPLICADO_MENSAJE, libro.getIsbn()));
        }
    }

    @Override
    public void borrarLibro(Long libroId) {
        LOGGER.info("LibroServiceImpl.borrarLibro: borrando el libro con id {}", libroId);

        libroRepository.delete(
                libroRepository.findById(libroId).orElseThrow(
                        () -> new LibroException(500, String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroId))
                ));
        LOGGER.info("LibroServiceImpl.borrarLibro: se ha borrado libro con id {}", libroId);
    }
}
