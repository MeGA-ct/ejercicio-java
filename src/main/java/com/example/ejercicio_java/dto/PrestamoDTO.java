package com.example.ejercicio_java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class PrestamoDTO {

    private Long id;

    private UsuarioDTO usuario;

    private LibroDTO libro;

    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucion;
}
