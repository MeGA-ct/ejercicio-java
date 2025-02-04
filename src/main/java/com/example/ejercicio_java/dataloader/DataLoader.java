package com.example.ejercicio_java.dataloader;

import com.example.ejercicio_java.dao.LibroDAO;
import com.example.ejercicio_java.repository.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {
    private final LibroRepository libroRepository;

    public DataLoader(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 3; i++) {
            String titulo  = "titulo " + i;
            String autor = "autor " + i;
            String isbn = "isbn " + i;
            LocalDate fechaPublicacion = LocalDate.now();
            LibroDAO libroDAO = new LibroDAO(titulo, autor, isbn, fechaPublicacion);
            libroRepository.save(libroDAO);
        }
    }
}
