package com.example.ejercicio_java.exceptions.usuario;

public class UsuarioException extends RuntimeException {

    public UsuarioException(int code, String mensaje)
    {
        super(String.format("ERROR (%d):  %s", code, mensaje));
    }
}
