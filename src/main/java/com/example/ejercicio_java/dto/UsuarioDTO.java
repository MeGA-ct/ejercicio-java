package com.example.ejercicio_java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class UsuarioDTO {

    private Long id;

    private String nombre;

    private String email;

    private String telefono;

    private LocalDate fechaRegistro;
}
