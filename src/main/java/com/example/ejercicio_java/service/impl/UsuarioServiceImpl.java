package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.UsuarioDAO;
import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.libro.LibroException;
import com.example.ejercicio_java.mapper.UsuarioMapper;
import com.example.ejercicio_java.repository.UsuarioRepository;
import com.example.ejercicio_java.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    public static final String USUARIO_NO_ENCONTRADO_MENSAJE = "El usuario con id[%d] no encontrado";
    public static final String USUARIO_EMAIL_DUPLICADO_MENSAJE = "Email [%S] ya está en uso";

    public static final Logger LOGGER = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private static final UsuarioMapper usuarioMapper = UsuarioMapper.INSTANCE;

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioDTO> obtenerUsuarios() {
        LOGGER.info("LibroServiceImpl.obtenerLibros: obteniendo todos los libros");

        List<UsuarioDAO> usuarios = usuarioRepository.findAll();

        List<UsuarioDTO> resultado = usuarioMapper.usuariosDaoToUsuariosDto(usuarios);

        LOGGER.info("LibroServiceImpl.obtenerLibros: se han obtenido {} libros.", resultado.size());
        return resultado;
    }

    @Override
    public UsuarioDTO obtenerUnUsuario(Long usuarioId) {
        return null;
    }

    @Override
    public UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO) {
        return null;
    }

    @Override
    public UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO) {
        return null;
    }

    @Override
    public UsuarioDTO actualizarParcialmenteUsuario(Long usuarioId, Map<String, Object> updates) {
        return null;
    }

    @Override
    public Void borrarUsuario(Long usuarioId) {
        LOGGER.info("LibroServiceImpl.borrarLibro: borrando el libro con id {}", usuarioId);

        usuarioRepository.delete(
                usuarioRepository.findById(usuarioId).orElseThrow(
                        () -> new LibroException(500, String.format(USUARIO_NO_ENCONTRADO_MENSAJE, usuarioId))
                ));
        LOGGER.info("LibroServiceImpl.borrarLibro: se ha borrado libro con id {}", usuarioId);
        return null;
    }
}
