package com.example.ejercicio_java.mapper;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dto.LibroDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LibroMapper {
    LibroMapper INSTANCE = Mappers.getMapper(LibroMapper.class);

    LibroDAO libroDtoToLibroDao(LibroDTO libroDto);

    LibroDTO libroDaoToLibroDto(LibroDAO libroDao);

    List<LibroDTO> librosDaoToLibrosDto(List<LibroDAO> libroDaoList);
}
