package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Orden;
import org.example.bearhouse_backend.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    // Órdenes por estado (ej. todas las ABIERTAS, todas las PAGADAS)
    List<Orden> findByEstadoOrderByFechaDesc(EstadoOrden estado);

    // Órdenes en un rango de fechas (para reportes de ventas por período)
    List<Orden> findByFechaBetweenOrderByFechaDesc(LocalDateTime desde, LocalDateTime hasta);

    // Órdenes pagadas en un rango (las ventas reales de un período)
    List<Orden> findByEstadoAndFechaBetween(EstadoOrden estado, LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Orden o " +
            "WHERE o.estado = 'PAGADA' AND o.fecha BETWEEN :desde AND :hasta")
    BigDecimal calcularVentasTotales(@Param("desde") LocalDateTime desde,
                                     @Param("hasta") LocalDateTime hasta);

    @Query("SELECT COUNT(o) FROM Orden o " +
            "WHERE o.estado = 'PAGADA' AND o.fecha BETWEEN :desde AND :hasta")
    Long contarOrdenesPagadas(@Param("desde") LocalDateTime desde,
                              @Param("hasta") LocalDateTime hasta);

    @Query("SELECT d.platillo.nombre, SUM(d.cantidad) AS totalVendido " +
            "FROM DetalleOrden d " +
            "WHERE d.orden.estado = 'PAGADA' " +
            "AND d.orden.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY d.platillo.id, d.platillo.nombre " +
            "ORDER BY totalVendido DESC")
    List<Object[]> platillosMasVendidos(@Param("desde") LocalDateTime desde,
                                        @Param("hasta") LocalDateTime hasta);

    @Query("SELECT o.tipo, COUNT(o), COALESCE(SUM(o.total), 0) " +
            "FROM Orden o " +
            "WHERE o.estado = 'PAGADA' AND o.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY o.tipo")
    List<Object[]> ventasPorTipo(@Param("desde") LocalDateTime desde,
                                 @Param("hasta") LocalDateTime hasta);
}