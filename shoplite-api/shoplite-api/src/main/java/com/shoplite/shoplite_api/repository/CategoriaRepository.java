package com.shoplite.shoplite_api.repository;

import com.shoplite.shoplite_api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}