package com.example.ejercicio_java.exceptions;

public class LibroNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Libro con id %s no encontrado";
    public LibroNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
    public LibroNotFoundException(String message) {
        super(message);
    }
}