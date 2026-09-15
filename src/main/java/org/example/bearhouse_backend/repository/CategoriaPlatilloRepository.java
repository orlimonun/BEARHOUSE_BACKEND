package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.CategoriaPlatillo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaPlatilloRepository extends JpaRepository<CategoriaPlatillo, Integer> {
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre, Integer id);

}