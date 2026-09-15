package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByProveedorIdOrderByFechaDesc(Integer proveedorId);

}
