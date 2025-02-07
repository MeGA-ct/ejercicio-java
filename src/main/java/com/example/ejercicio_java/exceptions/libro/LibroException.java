package com.example.ejercicio_java.exceptions.libro;


public class LibroException extends RuntimeException {
    public static final int NO_ENCONTRADO = 500;
    public static final int ISBN_DUPLICADO = 501;
    public static final int ESTA_EN_PRESTAMO = 502;

    public LibroException(int code, String mensaje)
    {
        super(String.format("ERROR (%d):  %s", code, mensaje));
    }
}
