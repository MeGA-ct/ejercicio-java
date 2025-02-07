package com.example.ejercicio_java.controller.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PrestamoBody(
        @NotNull
        Long usuarioId,
        @NotNull
        Long libroId,
        @NotNull
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucion
        ) {
}
