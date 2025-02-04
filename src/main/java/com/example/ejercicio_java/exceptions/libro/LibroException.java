package com.example.ejercicio_java.exceptions.libro;

public class LibroException extends RuntimeException {

    public LibroException(int code, String mensaje)
    {
        super(String.format("ERROR (%d):  %s", code, mensaje));
    }
}
