package com.example.ejercicio_java.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "prestamo")
public class PrestamoDAO {
    public static final String USUARIO_ID = "usuarioId";
    public static final String LIBRO_ID = "libroId";
    public static final String FECHA_PRESTAMO = "fechaPrestamo";
    public static final String FECHA_DEVOLUCION = "fechaDevolucion";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioDAO usuario;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private LibroDAO libro;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    public PrestamoDAO(UsuarioDAO usuario, LibroDAO libro, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }
}