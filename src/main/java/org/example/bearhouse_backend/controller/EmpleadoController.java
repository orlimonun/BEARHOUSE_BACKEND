package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.empleado.EmpleadoRequestDTO;
import org.example.bearhouse_backend.dto.empleado.EmpleadoResponseDTO;
import org.example.bearhouse_backend.service.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@PreAuthorize("hasRole('ADMIN')")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(
            @Valid @RequestBody EmpleadoRequestDTO requestDTO) {
        EmpleadoResponseDTO creado = empleadoService.crearEmpleado(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> listarEmpleados() {
        return ResponseEntity.ok(empleadoService.listarEmpleados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(empleadoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> actualizarEmpleado(
            @PathVariable Integer id,
            @Valid @RequestBody EmpleadoRequestDTO requestDTO) {
        return ResponseEntity.ok(empleadoService.actualizarEmpleado(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}