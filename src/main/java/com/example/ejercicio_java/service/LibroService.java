package com.example.ejercicio_java.service;

import com.example.ejercicio_java.dto.LibroDTO;

import java.util.List;

public interface LibroService {

    List<LibroDTO> obtenerLibros();

    LibroDTO obtenerUnLibro(final Long libroId);
}
