package com.example.ejercicio_java.repository;

import com.example.ejercicio_java.dao.PrestamoDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<PrestamoDAO, Long> {
}
