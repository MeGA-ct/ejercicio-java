package com.example.ejercicio_java.controller.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UsuarioBody(
        @NotNull
        String nombre,
        @NotNull
        String email,
        @NotNull
        String telefono,
        LocalDate fechaRegistro
        ) {
}
