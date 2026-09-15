package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecetaRepository extends JpaRepository<Receta, Integer> {

    // Todas las recetas (líneas) de un platillo: su receta completa
    List<Receta> findByPlatilloId(Integer platilloId);

    // Para validar la unicidad compuesta: ¿ya existe esta combinación platillo+ingrediente?
    boolean existsByPlatilloIdAndIngredienteId(Integer platilloId, Integer ingredienteId);

    // Para actualizar: ¿existe la combinación en OTRA receta (id distinto)?
    boolean existsByPlatilloIdAndIngredienteIdAndIdNot(Integer platilloId, Integer ingredienteId, Integer id);

    // Buscar una línea concreta por platillo+ingrediente (útil para quitar o editar)
    Optional<Receta> findByPlatilloIdAndIngredienteId(Integer platilloId, Integer ingredienteId);
}