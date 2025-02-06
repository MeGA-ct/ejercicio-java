package com.example.ejercicio_java.service;

import com.example.ejercicio_java.dto.PrestamoDTO;

import java.util.List;
import java.util.Map;

public interface PrestamoService {

    List<PrestamoDTO> obtenerPrestamos();

    PrestamoDTO obtenerUnPrestamo(final Long prestamoId);

    PrestamoDTO guardarPrestamo(final PrestamoDTO prestamoDTO);

    PrestamoDTO actualizarPrestamo(final PrestamoDTO prestamoDTO);

    PrestamoDTO actualizarParcialmentePrestamo(final Long prestamoId, final Map<String,Object> updates);

    Void borrarPrestamo(final Long prestamoId);
}
