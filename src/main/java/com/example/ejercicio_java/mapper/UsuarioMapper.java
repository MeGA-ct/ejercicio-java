package com.example.ejercicio_java.mapper;

import com.example.ejercicio_java.dao.UsuarioDAO;
import com.example.ejercicio_java.dto.UsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDAO usuarioDtoToUsuarioDao(UsuarioDTO usuarioDto);

    UsuarioDTO usuarioDaoToUsuarioDto(UsuarioDAO usuarioDao);

    List<UsuarioDTO> usuariosDaoToUsuariosDto(List<UsuarioDAO> usuarioDaoList);
}
