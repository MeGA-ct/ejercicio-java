package com.example.ejercicio_java.service;

import com.example.ejercicio_java.dto.LibroDTO;

import java.util.List;
import java.util.Map;

public interface LibroService {

    List<LibroDTO> obtenerLibros();

    LibroDTO obtenerUnLibro(final Long libroId);

    LibroDTO guardarLibro(final LibroDTO libroDTO);

    LibroDTO actualizarLibro(final LibroDTO libroDTO);

    LibroDTO actualizarParcialmenteLibro(final Long libroId, final Map<String,Object> updates);

    void borrarLibro(final Long libroId);
}
