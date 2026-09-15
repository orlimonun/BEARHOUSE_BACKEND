package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {

     boolean existsByNombre(String nombre);
     boolean existsByNombreAndIdNot(String nombre, Integer id);

    @Query("SELECT i FROM Ingrediente i WHERE i.stockActual <= i.stockMinimo AND i.activo = true")
    List<Ingrediente> findIngredientesBajoStock();


}
