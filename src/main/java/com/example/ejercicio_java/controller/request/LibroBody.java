package com.example.ejercicio_java.controller.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LibroBody(
        @NotNull
        String titulo,
        @NotNull
        String autor,
        @NotNull
        String isbn,
        LocalDate fechaPublicacion
        ) {
}
