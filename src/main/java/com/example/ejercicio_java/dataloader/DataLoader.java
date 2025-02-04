package com.example.ejercicio_java.dataloader;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.dao.UsuarioDAO;
import com.example.ejercicio_java.repository.LibroRepository;
import com.example.ejercicio_java.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public DataLoader(
            LibroRepository libroRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 3; i++) {
            String titulo = "titulo " + i;
            String autor = "autor " + i;
            String isbn = "isbn " + i;
            LocalDate fechaPublicacion = LocalDate.now();
            LibroDAO libroDAO = new LibroDAO(titulo, autor, isbn, fechaPublicacion);
            libroRepository.save(libroDAO);
        }

        for (int i = 1; i <= 3; i++) {
            String nombre = "nombre " + i;
            String email = "email" + i + "@mail.com";
            String telefono = "90090090" + i;
            LocalDate fechaRegistro = LocalDate.now();
            UsuarioDAO usuarioDAO = new UsuarioDAO(nombre, email, telefono, fechaRegistro);
            usuarioRepository.save(usuarioDAO);
        }

    }
}
