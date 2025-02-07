package com.example.ejercicio_java.mapper;

import com.example.ejercicio_java.dao.PrestamoDAO;
import com.example.ejercicio_java.dto.PrestamoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PrestamoMapper {
    PrestamoMapper INSTANCE = Mappers.getMapper(PrestamoMapper.class);

    PrestamoDAO prestamoDtoToPrestamoDao(PrestamoDTO prestamoDto);

    PrestamoDTO prestamoDaoToPrestamoDto(PrestamoDAO prestamoDao);

    List<PrestamoDTO> prestamosDaoToPrestamosDto(List<PrestamoDAO> prestamoDaoList);
}
