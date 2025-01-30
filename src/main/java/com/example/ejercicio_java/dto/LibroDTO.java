package com.example.ejercicio_java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class LibroDTO {

    private Long id;

    private String titulo;

    private String autor;

    private String isbn;

    private LocalDate fechaPublicacion;

}
