package com.example.ejercicio_java.repository;

import com.example.ejercicio_java.dao.UsuarioDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioDAO, Long> {
}
