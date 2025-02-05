package com.example.ejercicio_java.service;

import com.example.ejercicio_java.dto.UsuarioDTO;

import java.util.List;
import java.util.Map;

public interface UsuarioService {

    List<UsuarioDTO> obtenerUsuarios();

    UsuarioDTO obtenerUnUsuario(final Long usuarioId);

    UsuarioDTO guardarUsuario(final UsuarioDTO usuarioDTO);

    UsuarioDTO actualizarUsuario(final UsuarioDTO usuarioDTO);

    UsuarioDTO actualizarParcialmenteUsuario(final Long usuarioId, final Map<String,Object> updates);

    Void borrarUsuario(final Long usuarioId);
}
