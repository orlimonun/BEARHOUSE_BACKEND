package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {

     boolean existsByNombre(String nombre);
     boolean existsByNombreAndIdNot(String nombre, Integer id);

    @Query("SELECT i FROM Ingrediente i WHERE i.stockActual <= i.stockMinimo AND i.activo = true")
    List<Ingrediente> findIngredientesBajoStock();

    @Query("SELECT d.ingrediente.nombre, d.ingrediente.unidadMedida, " +
            "SUM(d.cantidad), COALESCE(SUM(d.subtotal), 0) " +
            "FROM DetalleCompra d " +
            "WHERE d.compra.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY d.ingrediente.id, d.ingrediente.nombre, d.ingrediente.unidadMedida " +
            "ORDER BY SUM(d.subtotal) DESC")
    List<Object[]> ingredientesMasComprados(@Param("desde") LocalDateTime desde,
                                            @Param("hasta") LocalDateTime hasta);

}
