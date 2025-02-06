package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.PrestamoDAO;
import com.example.ejercicio_java.dto.PrestamoDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.exceptions.prestamo.PrestamoException;
import com.example.ejercicio_java.mapper.LibroMapper;
import com.example.ejercicio_java.mapper.PrestamoMapper;
import com.example.ejercicio_java.mapper.UsuarioMapper;
import com.example.ejercicio_java.repository.LibroRepository;
import com.example.ejercicio_java.repository.PrestamoRepository;
import com.example.ejercicio_java.repository.UsuarioRepository;
import com.example.ejercicio_java.service.PrestamoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.example.ejercicio_java.service.impl.LibroServiceImpl.LIBRO_NO_ENCONTRADO_MENSAJE;
import static com.example.ejercicio_java.service.impl.UsuarioServiceImpl.USUARIO_NO_ENCONTRADO_MENSAJE;

@Service
public class PrestamoServiceImpl implements PrestamoService {
    public static final String PRESTAMO_NO_ENCONTRADO_MENSAJE = "El préstamo con id[%d] no encontrado";

    public static final Logger LOGGER = LoggerFactory.getLogger(PrestamoServiceImpl.class);

    private static final LibroMapper libroMapper = LibroMapper.INSTANCE;
    private static final UsuarioMapper usuarioMapper = UsuarioMapper.INSTANCE;
    private static final PrestamoMapper prestamoMapper = PrestamoMapper.INSTANCE;

    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;


    @Autowired
    public PrestamoServiceImpl(
            LibroRepository libroRepository,
            UsuarioRepository usuarioRepository,
            PrestamoRepository prestamoRepository
    ) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public List<PrestamoDTO> obtenerPrestamos() {
        LOGGER.info("PrestamoServiceImpl.obtenerPrestamos: obteniendo todos los préstamos");

        List<PrestamoDAO> prestamos = prestamoRepository.findAll();

        LOGGER.info("PrestamoServiceImpl.obtenerPrestamos: se han obtenido {} prestamos.", prestamos.size());
        return prestamoMapper.prestamosDaoToPrestamosDto(prestamos);
    }

    @Override
    public PrestamoDTO obtenerUnPrestamo(Long prestamoId) {
        LOGGER.info("PrestamoServiceImpl.obtenerUnPrestamo: obteniendo un préstamo");

        PrestamoDAO prestamo = prestamoRepository.findById(prestamoId).orElseThrow(
                () -> new PrestamoException(
                        PrestamoException.NO_ENCONTRADO,
                        String.format(PRESTAMO_NO_ENCONTRADO_MENSAJE, prestamoId)
                )
        );
        LOGGER.info("PrestamoServiceImpl.obtenerUnPrestamo: préstamo id {} obtenido", prestamo.getId());

        return prestamoMapper.prestamoDaoToPrestamoDto(prestamo);
    }

    @Override
    public PrestamoDTO guardarPrestamo(PrestamoDTO prestamoDTO) {
        LOGGER.info("PrestamoServiceImpl.guardarPrestamo: guardando info de un préstamo");
        PrestamoDAO prestamoGuardado = prestamoRepository.save(prestamoMapper.prestamoDtoToPrestamoDao(prestamoDTO));

        LOGGER.info("PrestamoServiceImpl.guardarPrestamo: nuevo prestamo creado, id: {}", prestamoGuardado.getId());
        return prestamoMapper.prestamoDaoToPrestamoDto(prestamoGuardado);
    }

    @Override
    public PrestamoDTO actualizarPrestamo(PrestamoDTO prestamoDTO) {
        LOGGER.info("PrestamoServiceImpl.actualizarPrestamo: actualizando el préstamo con id {}", prestamoDTO.getId());

        PrestamoDAO prestamo = prestamoRepository.findById(prestamoDTO.getId()).orElseThrow(
                () -> new PrestamoException(
                        PrestamoException.NO_ENCONTRADO,
                        String.format(PRESTAMO_NO_ENCONTRADO_MENSAJE, prestamoDTO.getId())
                ));
        prestamo.setUsuario(usuarioMapper.usuarioDtoToUsuarioDao(prestamoDTO.getUsuario()));
        prestamo.setLibro(libroMapper.libroDtoToLibroDao(prestamoDTO.getLibro()));
        prestamo.setFechaPrestamo(prestamoDTO.getFechaPrestamo());
        prestamo.setFechaDevolucion(prestamoDTO.getFechaDevolucion());

        PrestamoDAO prestamoGuardado = prestamoRepository.save(prestamo);

        LOGGER.info("PrestamoServiceImpl.actualizarPrestamo: préstamo con id {} actualizado", prestamoGuardado.getId());

        return prestamoMapper.prestamoDaoToPrestamoDto(prestamoGuardado);

    }

    @Override
    public PrestamoDTO actualizarParcialmentePrestamo(Long prestamoId, Map<String, Object> updates) {
        LOGGER.info(
                "PrestamoServiceImpl.actualizarParcialmentePrestamo: actualizando el préstamo con id {}",
                prestamoId
        );

        PrestamoDAO prestamo = prestamoRepository.findById(prestamoId).orElseThrow(
                () -> new PrestamoException(
                        PrestamoException.NO_ENCONTRADO,
                        String.format(PRESTAMO_NO_ENCONTRADO_MENSAJE, prestamoId)
                ));

        if (updates.containsKey(PrestamoDAO.USUARIO_ID)) {
            Long usuarioId = Long.valueOf(updates.get(PrestamoDAO.USUARIO_ID).toString());
            prestamo.setUsuario(
                    usuarioRepository.findById(usuarioId).orElseThrow(
                            () -> new PrestamoException(
                                    PrestamoException.NO_ENCONTRADO,
                                    String.format(USUARIO_NO_ENCONTRADO_MENSAJE, usuarioId)
                            )
                    )
            );
        }
        if (updates.containsKey(PrestamoDAO.LIBRO_ID)) {
            Long libroId = Long.valueOf(updates.get(PrestamoDAO.LIBRO_ID).toString());
            prestamo.setLibro(
                    libroRepository.findById(libroId).orElseThrow(
                            () -> new LibroException(
                                    LibroException.NO_ENCONTRADO,
                                    String.format(LIBRO_NO_ENCONTRADO_MENSAJE, libroId)
                            )
                    )
            );
        }
        if (updates.containsKey(PrestamoDAO.FECHA_PRESTAMO)) {
            prestamo.setFechaPrestamo(LocalDate.parse(updates.get(PrestamoDAO.FECHA_PRESTAMO).toString()));
        }
        if (updates.containsKey(PrestamoDAO.FECHA_DEVOLUCION)) {
            prestamo.setFechaPrestamo(LocalDate.parse(updates.get(PrestamoDAO.FECHA_DEVOLUCION).toString()));
        }

        PrestamoDAO prestamoGuardado = prestamoRepository.save(prestamo);

        LOGGER.info(
                "PrestamoServiceImpl.actualizarParcialmentePrestamo: el préstamo con id {} actualizado",
                prestamoGuardado.getId()
        );

        return prestamoMapper.prestamoDaoToPrestamoDto(prestamoGuardado);
    }

    @Override
    public Void borrarPrestamo(Long prestamoId) {
        LOGGER.info("PrestamoServiceImpl.borrarPrestamo: borrando el préstamo con id {}", prestamoId);

        prestamoRepository.delete(
                prestamoRepository.findById(prestamoId).orElseThrow(
                        () -> new PrestamoException(
                                PrestamoException.NO_ENCONTRADO,
                                String.format(PRESTAMO_NO_ENCONTRADO_MENSAJE, prestamoId)
                        )
                )
        );
        LOGGER.info("PrestamoServiceImpl.borrarPrestamo: se ha borrado el préstamo con id {}", prestamoId);
        return null;
    }
}
