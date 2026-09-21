package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByProveedorIdOrderByFechaDesc(Integer proveedorId);

    @Query("SELECT COALESCE(SUM(c.total), 0) FROM Compra c " +
            "WHERE c.fecha BETWEEN :desde AND :hasta")
    BigDecimal calcularGastoTotal(@Param("desde") LocalDateTime desde,
                                  @Param("hasta") LocalDateTime hasta);

    @Query("SELECT COUNT(c) FROM Compra c " +
            "WHERE c.fecha BETWEEN :desde AND :hasta")
    Long contarCompras(@Param("desde") LocalDateTime desde,
                       @Param("hasta") LocalDateTime hasta);

    @Query("SELECT c.proveedor.nombre, COUNT(c), COALESCE(SUM(c.total), 0) " +
            "FROM Compra c " +
            "WHERE c.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY c.proveedor.id, c.proveedor.nombre " +
            "ORDER BY SUM(c.total) DESC")
    List<Object[]> gastoPorProveedor(@Param("desde") LocalDateTime desde,
                                     @Param("hasta") LocalDateTime hasta);

}
