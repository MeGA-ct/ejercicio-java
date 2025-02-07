package com.example.ejercicio_java.exceptions.prestamo;

public class PrestamoException extends RuntimeException {
    public static final int NO_ENCONTRADO = 504;

    public PrestamoException(int code, String mensaje)
    {
        super(String.format("ERROR (%d):  %s", code, mensaje));
    }
}
