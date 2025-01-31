package com.example.ejercicio_java.exceptions;

public class LibroNotFoundException extends RuntimeException {
    public static final int ERROR_CODE = 101;
    private static final String MESSAGE
            = "Libro con id %s no encontrado";
    public LibroNotFoundException(Long id) {
        super(String.format(ERROR_CODE + ": " + MESSAGE, id));
    }
    public LibroNotFoundException(String message) {
        super(message);
    }
}