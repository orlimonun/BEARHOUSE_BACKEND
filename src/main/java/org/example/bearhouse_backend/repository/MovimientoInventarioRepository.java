package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepository
        extends JpaRepository<MovimientoInventario, Integer> {

    List<MovimientoInventario> findByIngredienteIdOrderByFechaDesc(Integer ingredienteId);

}