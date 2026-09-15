package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Platillo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatilloRepository extends JpaRepository<Platillo, Integer> {
    List<Platillo> findByDisponibleTrue();
}

