package org.example.bearhouse_backend.repository;

import org.example.bearhouse_backend.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    boolean existsByUsuario(String usuario);
    boolean existsByCorreo(String correo);
    boolean existsByCedula(String cedula);

    boolean existsByUsuarioAndIdNot(String usuario, Integer id);
    boolean existsByCorreoAndIdNot(String correo, Integer id);
    boolean existsByCedulaAndIdNot(String cedula, Integer id);

    Optional<Empleado> findByUsuario(String usuario);

    @Query("SELECT e FROM Empleado e JOIN FETCH e.rol WHERE e.usuario = :usuario")
    Optional<Empleado> findByUsuarioConRol(@Param("usuario") String usuario);
}
