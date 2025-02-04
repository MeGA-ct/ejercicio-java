package com.example.ejercicio_java.controller.request;

import java.time.LocalDate;

public record LibroBody(
        String titulo,
        String autor,
        String isbn,
        LocalDate fechaPublicacion
        ) {
}
