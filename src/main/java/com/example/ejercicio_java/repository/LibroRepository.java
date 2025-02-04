package com.example.ejercicio_java.repository;

import com.example.ejercicio_java.dao.LibroDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<LibroDAO, Long> {
}
