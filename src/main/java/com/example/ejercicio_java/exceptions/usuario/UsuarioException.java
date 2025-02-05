package com.example.ejercicio_java.exceptions.usuario;

public class UsuarioException extends RuntimeException {
    public static final int NO_ENCONTRADO = 502;
    public static final int EMAIL_DUPLICADO = 503;

    public UsuarioException(int code, String mensaje)
    {
        super(String.format("ERROR (%d):  %s", code, mensaje));
    }
}
