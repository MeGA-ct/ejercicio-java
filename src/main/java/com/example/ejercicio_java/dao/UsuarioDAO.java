package com.example.ejercicio_java.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class UsuarioDAO {
    public static final String C_NOMBRE = "nombre";
    public static final String C_EMAIL = "email";
    public static final String C_TELEFONO = "telefono";
    public static final String C_FECHA_REGISTRO = "fechaRegistro";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 13)
    private String telefono;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    public UsuarioDAO(String nombre, String email, String telefono, LocalDate fechaRegistro) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
    }
}
