package com.example.ejercicio_java.dataloader;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dao.PrestamoDAO;
import com.example.ejercicio_java.dao.UsuarioDAO;
import com.example.ejercicio_java.repository.LibroRepository;
import com.example.ejercicio_java.repository.PrestamoRepository;
import com.example.ejercicio_java.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public DataLoader(
            LibroRepository libroRepository,
            UsuarioRepository usuarioRepository,
            PrestamoRepository prestamoRepository
    ) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<LibroDAO> libros = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String titulo = "titulo " + i;
            String autor = "autor " + i;
            String isbn = "isbn " + i;
            LocalDate fechaPublicacion = LocalDate.now();
            libros.add(libroRepository.save(new LibroDAO(titulo, autor, isbn, fechaPublicacion)));
        }

        List<UsuarioDAO> usuarios = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String nombre = "nombre " + i;
            String email = "email" + i + "@mail.com";
            String telefono = "90090090" + i;
            LocalDate fechaRegistro = LocalDate.now();
            usuarios.add(usuarioRepository.save(new UsuarioDAO(nombre, email, telefono, fechaRegistro)));
        }

        for (int i = 0; i < 3; i++) {
            UsuarioDAO usuarioPrestamo = usuarios.get(i);
            LibroDAO libroPrestamo = libros.get(i);
            LocalDate fechaPrestamo = LocalDate.now().minusDays(7L*i);
            LocalDate fechaDevolucion = LocalDate.now();
            prestamoRepository.save(new PrestamoDAO(
                    usuarioPrestamo,
                    libroPrestamo,
                    fechaPrestamo,
                    (i != 0 ) ? fechaDevolucion : null
            ));
        }
    }
}
