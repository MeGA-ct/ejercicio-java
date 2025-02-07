package com.example.ejercicio_java.service.impl;

import com.example.ejercicio_java.dao.UsuarioDAO;
import com.example.ejercicio_java.dto.UsuarioDTO;
import com.example.ejercicio_java.exceptions.usuario.UsuarioException;
import com.example.ejercicio_java.mapper.UsuarioMapper;
import com.example.ejercicio_java.repository.UsuarioRepository;
import com.example.ejercicio_java.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    public static final String USUARIO_NO_ENCONTRADO_MENSAJE = "El usuario con id[%d] no encontrado";
    public static final String USUARIO_EMAIL_DUPLICADO_MENSAJE = "Email [%S] ya está en uso";
    public static final String USUARIO_CON_PRESTAMOS_MENSAJE = "El usuario con id[%d] tiene préstamos";

    public static final Logger LOGGER = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private static final UsuarioMapper usuarioMapper = UsuarioMapper.INSTANCE;

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioDTO> obtenerUsuarios() {
        LOGGER.info("UsuarioServiceImpl.obtenerUsuarios: obteniendo todos los usuarios");

        List<UsuarioDAO> usuarios = usuarioRepository.findAll();

        LOGGER.info("UsuarioServiceImpl.obtenerUsuarios: se han obtenido {} usuarios.", usuarios.size());
        return usuarioMapper.usuariosDaoToUsuariosDto(usuarios);
    }

    @Override
    public UsuarioDTO obtenerUnUsuario(Long usuarioId) {
        LOGGER.info("UsuarioServiceImpl.obtenerUnUsuario: obteniendo un usuario");

        UsuarioDAO usuario = usuarioRepository.findById(usuarioId).orElseThrow(
                () -> new UsuarioException(
                        UsuarioException.NO_ENCONTRADO,
                        String.format(USUARIO_NO_ENCONTRADO_MENSAJE, usuarioId)
                ));

        LOGGER.info("UsuarioServiceImpl.obtenerUnUsuario: usuario con email {} obtenido", usuario.getEmail());

        return usuarioMapper.usuarioDaoToUsuarioDto(usuario);
    }

    @Override
    public UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO) throws UsuarioException {
        try {
            UsuarioDAO usuarioGuardado = usuarioRepository.save(usuarioMapper.usuarioDtoToUsuarioDao(usuarioDTO));

            LOGGER.info("UsuarioServiceImpl.guardarUsuario: nuevo usuario creado, id: {}", usuarioGuardado.getId());

            return usuarioMapper.usuarioDaoToUsuarioDto(usuarioGuardado);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("UsuarioServiceImpl.guardarUsuario: email {} Duplicado", usuarioDTO.getEmail());
            throw new UsuarioException(
                    UsuarioException.EMAIL_DUPLICADO,
                    String.format(USUARIO_EMAIL_DUPLICADO_MENSAJE, usuarioDTO.getEmail())
            );
        }
    }

    @Override
    public UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO) throws UsuarioException {
        LOGGER.info("UsuarioServiceImpl.actualizarUsuario: actualizando usuario con id {}", usuarioDTO.getId());
        UsuarioDAO usuario = usuarioRepository.findById(usuarioDTO.getId()).orElseThrow(
                () -> new UsuarioException(
                        UsuarioException.NO_ENCONTRADO,
                        String.format(USUARIO_NO_ENCONTRADO_MENSAJE, usuarioDTO.getId())
                ));
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());

        try {
            UsuarioDAO usuarioGuardado = usuarioRepository.save(usuario);
            LOGGER.info("UsuarioServiceImpl.actualizarUsuario: usuario con id {} actualizado", usuarioGuardado.getId());
            return usuarioMapper.usuarioDaoToUsuarioDto(usuarioGuardado);

        } catch (DataIntegrityViolationException e) {
            throw new UsuarioException(
                    UsuarioException.EMAIL_DUPLICADO,
                    String.format(USUARIO_EMAIL_DUPLICADO_MENSAJE, usuarioDTO.getEmail())
            );
        }
    }

    @Override
    public UsuarioDTO actualizarParcialmenteUsuario(Long usuarioId, Map<String, Object> updates)
            throws UsuarioException {
        LOGGER.info("UsuarioServiceImpl.actualizarParcialmenteUsuario: actualizando usuario con id {}", usuarioId);
        UsuarioDAO usuario = usuarioRepository.findById(usuarioId).orElseThrow(
                () -> new UsuarioException(
                        UsuarioException.NO_ENCONTRADO,
                        String.format(USUARIO_NO_ENCONTRADO_MENSAJE, usuarioId)
                ));

        if (updates.containsKey(UsuarioDAO.C_NOMBRE)) {
            usuario.setNombre(updates.get(UsuarioDAO.C_NOMBRE).toString());
        }
        if (updates.containsKey(UsuarioDAO.C_EMAIL)) {
            usuario.setEmail(updates.get(UsuarioDAO.C_EMAIL).toString());
        }
        if (updates.containsKey(UsuarioDAO.C_TELEFONO)) {
            usuario.setTelefono(updates.get(UsuarioDAO.C_TELEFONO).toString());
        }
        if (updates.containsKey(UsuarioDAO.C_FECHA_REGISTRO)) {
            usuario.setFechaRegistro(LocalDate.parse(updates.get(UsuarioDAO.C_FECHA_REGISTRO).toString()));
        }

        try {
            UsuarioDAO usuarioGuardado = usuarioRepository.save(usuario);
            LOGGER.info(
                    "UsuarioServiceImpl.actualizarParcialmenteUsuario: usuario con id {} actualizado",
                    usuarioGuardado.getId()
            );
            return usuarioMapper.usuarioDaoToUsuarioDto(usuarioGuardado);

        } catch (DataIntegrityViolationException e) {
            throw new UsuarioException(
                    UsuarioException.EMAIL_DUPLICADO,
                    String.format(USUARIO_EMAIL_DUPLICADO_MENSAJE, updates.get(UsuarioDAO.C_EMAIL).toString())
            );
        }
    }

    @Override
    public Void borrarUsuario(Long usuarioId) {
        LOGGER.info("UsuarioServiceImpl.borrarUsuario: borrando el libro con id {}", usuarioId);

        try {
            usuarioRepository.delete(
                    usuarioRepository.findById(usuarioId).orElseThrow(
                            () -> new UsuarioException(
                                    UsuarioException.NO_ENCONTRADO,
                                    String.format(USUARIO_NO_ENCONTRADO_MENSAJE, usuarioId)
                            )
                    ));
            LOGGER.info("UsuarioServiceImpl.borrarUsuario: se ha borrado libro con id {}", usuarioId);
        } catch (DataIntegrityViolationException e) {
            throw new UsuarioException(
                    UsuarioException.TIENE_PRESTAMOS,
                    String.format(USUARIO_CON_PRESTAMOS_MENSAJE, usuarioId)
            );
        }

        return null;
    }
}
