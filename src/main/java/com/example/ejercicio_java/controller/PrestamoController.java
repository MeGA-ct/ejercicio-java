package com.example.ejercicio_java.controller;

import ch.qos.logback.core.util.StringUtil;
import com.example.ejercicio_java.controller.request.PrestamoBody;
import com.example.ejercicio_java.dao.PrestamoDAO;
import com.example.ejercicio_java.dto.PrestamoDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.exceptions.prestamo.PrestamoException;
import com.example.ejercicio_java.exceptions.usuario.UsuarioException;
import com.example.ejercicio_java.service.LibroService;
import com.example.ejercicio_java.service.PrestamoService;
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
@RequestMapping(path = "/prestamos")
public class PrestamoController {

    private final UsuarioService usuarioService;
    private final LibroService libroService;
    private final PrestamoService prestamoService;

    public static final Logger LOGGER = LoggerFactory.getLogger(PrestamoController.class);

    @Autowired
    public PrestamoController(
            UsuarioService usuarioService,
            LibroService libroService,
            PrestamoService prestamoService
    ) {
        this.usuarioService = usuarioService;
        this.libroService = libroService;
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoDTO>> obtenerPrestamos() {

        LOGGER.info("PrestamoController.obtenerPrestamos: obteniendo todos los préstamos");

        List<PrestamoDTO> resultado = prestamoService.obtenerPrestamos();

        LOGGER.info("PrestamoController.obtenerPrestamos: hay {} préstamos.", resultado.size());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> obtenerUnPrestamo(@PathVariable("id") Long prestamoId) {
        LOGGER.info("PrestamoController.obtenerUnPrestamo: obteniendo un préstamo");

        try {
            PrestamoDTO resultado = prestamoService.obtenerUnPrestamo(prestamoId);

            LOGGER.info(
                    "PrestamoController.obtenerUnPrestamo: id {} fecha {}",
                    resultado.getId(),
                    resultado.getFechaPrestamo()
            );

            return ResponseEntity.ok(resultado);
        } catch (PrestamoException e) {
            LOGGER.info("PrestamoController.obtenerUnPrestamo: error al obtener préstamo id {}.", prestamoId);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> guardarPrestamo(@RequestBody PrestamoBody prestamo) {
        LOGGER.info("PrestamoController.guardarPrestamo: guardando un nuevo préstamo");
        try {
            PrestamoDTO prestamoDTO = new PrestamoDTO(
                    null,
                    usuarioService.obtenerUnUsuario(prestamo.usuarioId()),
                    libroService.obtenerUnLibro(prestamo.libroId()),
                    prestamo.fechaPrestamo(),
                    prestamo.fechaDevolucion()
            );

            PrestamoDTO resultado = prestamoService.guardarPrestamo(prestamoDTO);

            LOGGER.info("PrestamoController.guardarPrestamo: creado el préstamo con id {}", resultado.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (UsuarioException | LibroException e) {
            LOGGER.info("PrestamoController.guardarPrestamo: error al crear préstamo {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> actualizarPrestamo(
            @PathVariable("id") Long prestamoId,
            @RequestBody PrestamoBody prestamo
    ) {
        LOGGER.info("PrestamoController.actualizarPrestamo: actualizando préstamo {}", prestamoId);
        try {
            PrestamoDTO prestamoDTO = new PrestamoDTO(
                    prestamoId,
                    usuarioService.obtenerUnUsuario(prestamo.usuarioId()),
                    libroService.obtenerUnLibro(prestamo.libroId()),
                    prestamo.fechaPrestamo(),
                    prestamo.fechaDevolucion()
            );
            PrestamoDTO resultado = prestamoService.actualizarPrestamo(prestamoDTO);

            LOGGER.info("PrestamoController.actualizarPrestamo: actualizado el préstamo con id {}", resultado.getId());
            return ResponseEntity.ok(resultado);
        } catch (UsuarioException | LibroException | PrestamoException e) {
            LOGGER.info("PrestamoController.actualizarPrestamo: error al actualizar el préstamo {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> actualizarParcialmentePrestamo(
            @PathVariable("id") Long prestamoId,
            @RequestBody Map<String, Object> updates
    ) {
        LOGGER.info(
                "PrestamoController.actualizarParcialmentePrestamo: actualización parcial del préstamo {}",
                prestamoId
        );

        if (StringUtil.isNullOrEmpty(updates.get(PrestamoDAO.USUARIO_ID).toString())){
            updates.remove(PrestamoDAO.USUARIO_ID);
        }
        if (StringUtil.isNullOrEmpty(updates.get(PrestamoDAO.LIBRO_ID).toString())){
            updates.remove(PrestamoDAO.LIBRO_ID);
        }

        try {
            PrestamoDTO resultado = prestamoService.actualizarParcialmentePrestamo(prestamoId, updates);
            LOGGER.info(
                    "PrestamoController.actualizarParcialmentePrestamo: actualizado parcialmente el préstamo {}",
                    resultado.getId()
            );
            return ResponseEntity.ok(resultado);
        } catch (UsuarioException | LibroException | PrestamoException e) {
            LOGGER.info("PrestamoController.actualizarParcialmentePrestamo: error al modificar préstamo {}", prestamoId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> borrarPrestamo(
            @PathVariable("id") Long prestamoId
    ) {
        LOGGER.info("PrestamoController.borrarPrestamo: borrando el préstamo con id {}", prestamoId);
        try {
            prestamoService.borrarPrestamo(prestamoId);
            LOGGER.info("PrestamoController.borrarPrestamo: se ha borrado préstamo con id {}", prestamoId);
            return ResponseEntity.noContent().build();
        } catch (PrestamoException e) {
            LOGGER.info("PrestamoController.borrarPrestamo: error al borrar préstamo con id {}", prestamoId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
