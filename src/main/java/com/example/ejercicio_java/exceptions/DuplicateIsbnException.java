package com.example.ejercicio_java.exceptions;

public class DuplicateIsbnException extends RuntimeException {
    private static final String MESSAGE = "ISBN %s ya introducido en otro Libro.";
    public DuplicateIsbnException(String isbn) {
        super(String.format(MESSAGE, isbn));
    }
}
