package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.CategoriaIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriaIngredienteRepository extends JpaRepository<CategoriaIngrediente, Integer> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Integer id);

}
