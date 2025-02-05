package com.example.ejercicio_java.controller.request;

import java.time.LocalDate;

public record UsuarioBody(
        String nombre,
        String email,
        String telefono,
        LocalDate fechaRegistro
        ) {
}
