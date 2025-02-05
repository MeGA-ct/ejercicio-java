package com.example.ejercicio_java.exceptions.libro;


public class LibroException extends RuntimeException {
    public static final int NO_ENCONTRADO = 500;
    public static final int ISBN_DUPLICADO = 501;

    public LibroException(int code, String mensaje)
    {
        super(String.format("ERROR (%d):  %s", code, mensaje));
    }
}
