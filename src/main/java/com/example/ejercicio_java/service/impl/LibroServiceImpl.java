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

    public static final String LIBRO_NO_ENCONTRADO_MENSAJE = "El libro con id[%d] no encontrado";
    public static final String LIBRO_ISBN_DUPLICADO_MENSAJE = "ISBN [%S] ya introducido en otro libro";
    public static final String LIBRO_EN_PRESTAMO_MENSAJE = "El libro con [%S] tiene prestamos";

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

        LOGGER.info("LibroServiceImpl.obtenerLibros: se han obtenido {} libros.", libros.size());

        return libroMapper.librosDaoToLibrosDto(libros);
    }

    @Override
    public LibroDTO obtenerUnLibro(final Long libroId) {
        LOGGER.info("LibroServiceImpl.obtenerUnLibro: obteniendo un solo libro");

        LibroDAO libro = libroRepository.findById(libroId).orElseThrow(
                () -> new LibroException(
                        LibroException.NO_ENCONTRADO,
                        String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroId)
                )
        );

        LOGGER.info("LibroServiceImpl.obtenerUnLibro: libro {} por {} obtenido", libro.getTitulo(), libro.getAutor());

        return libroMapper.libroDaoToLibroDto(libro);
    }

    @Override
    public LibroDTO guardarLibro(LibroDTO libroDTO) throws LibroException {
        LOGGER.info("LibroServiceImpl.guardarLibro: guardando libro");

        try {
            LibroDAO libroGuardado = libroRepository.save(libroMapper.libroDtoToLibroDao(libroDTO));

            LOGGER.info("LibroServiceImpl.guardarLibro: libro con id {} guardado", libroGuardado.getId());

            return libroMapper.libroDaoToLibroDto(libroGuardado);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("LibroServiceImpl.guardarLibro: ISBN Duplicado");
            throw new LibroException(
                    LibroException.ISBN_DUPLICADO,
                    String.format(LIBRO_ISBN_DUPLICADO_MENSAJE, libroDTO.getIsbn())
            );
        }
    }

    @Override
    public LibroDTO actualizarLibro(LibroDTO libroDTO) throws LibroException {
        LOGGER.info("LibroServiceImpl.actualizarLibro: actualizando libro");
        LibroDAO libro = libroRepository
                .findById(libroDTO.getId())
                .orElseThrow(
                        () -> new LibroException(
                                LibroException.NO_ENCONTRADO,
                                String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroDTO.getId())
                        )
                );

        libro.setTitulo(libroDTO.getTitulo());
        libro.setAutor(libroDTO.getAutor());
        libro.setIsbn(libroDTO.getIsbn());
        libro.setFechaPublicacion(libroDTO.getFechaPublicacion());
        try {
            LibroDAO libroGuardado = libroRepository.save(libro);

            LOGGER.info("LibroServiceImpl.actualizarLibro: libro con id {} obtenido", libroGuardado.getId());

            return libroMapper.libroDaoToLibroDto(libroGuardado);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("LibroServiceImpl.actualizarLibro: ISBN Duplicado");
            throw new LibroException(
                    LibroException.ISBN_DUPLICADO,
                    String.format(LIBRO_ISBN_DUPLICADO_MENSAJE, libroDTO.getIsbn())
            );
        }
    }

    @Override
    public LibroDTO actualizarParcialmenteLibro(Long libroId, Map<String, Object> updates) throws LibroException {
        LOGGER.info("LibroServiceImpl.actualizarParcialmenteLibro: actualizando parcialmente libro");

        LibroDAO libro = libroRepository.findById(libroId).orElseThrow(
                () -> new LibroException(
                        LibroException.NO_ENCONTRADO,
                        String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroId)
                )
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
            LibroDAO libroGuardado = libroRepository.save(libro);
            LOGGER.info(
                    "LibroServiceImpl.actualizarParcialmenteLibro: libro con id {} obtenido",
                    libroGuardado.getId()
            );

            return libroMapper.libroDaoToLibroDto(libroGuardado);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("LibroServiceImpl.actualizarParcialmenteLibro: ISBN Duplicado");
            throw new LibroException(
                    LibroException.ISBN_DUPLICADO,
                    String.format(LIBRO_ISBN_DUPLICADO_MENSAJE, updates.get("isbn").toString())
            );
        }
    }

    @Override
    public Void borrarLibro(Long libroId) throws LibroException{
        LOGGER.info("LibroServiceImpl.borrarLibro: borrando el libro con id {}", libroId);

        try {
            libroRepository.delete(
                    libroRepository.findById(libroId).orElseThrow(
                            () -> new LibroException(
                                    LibroException.NO_ENCONTRADO,
                                    String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroId)
                            )
                    ));
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("LibroServiceImpl.actualizarParcialmenteLibro: Libro existe en un préstamo");
            throw new LibroException(
                    LibroException.ESTA_EN_PRESTAMO,
                    String.format(LIBRO_EN_PRESTAMO_MENSAJE, libroId)
            );
        }
        LOGGER.info("LibroServiceImpl.borrarLibro: se ha borrado libro con id {}", libroId);
        return null;
    }
}
