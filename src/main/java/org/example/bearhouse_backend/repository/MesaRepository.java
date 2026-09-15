package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepository extends JpaRepository<Mesa, Integer> {
    boolean existsByNumero(Integer numero);
    boolean existsByNumeroAndIdNot(Integer numero, Integer id);
}