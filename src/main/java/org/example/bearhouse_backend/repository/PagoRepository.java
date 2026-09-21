package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByOrdenIdOrderByFechaAsc(Integer ordenId);
}