package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.ingrediente.IngredienteRequestDTO;
import org.example.bearhouse_backend.dto.ingrediente.IngredienteResponseDTO;
import org.example.bearhouse_backend.service.IngredienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BODEGUERO')")
    public ResponseEntity<IngredienteResponseDTO> crearIngrediente(
            @Valid @RequestBody IngredienteRequestDTO requestDTO) {
        IngredienteResponseDTO creado = ingredienteService.crearIngrediente(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BODEGUERO', 'COCINA')")
    public ResponseEntity<List<IngredienteResponseDTO>> listarIngredientes() {
        return ResponseEntity.ok(ingredienteService.listarIngredientes());
    }

    @GetMapping("/bajo-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'BODEGUERO')")
    public ResponseEntity<List<IngredienteResponseDTO>> listarBajoStock() {
        return ResponseEntity.ok(ingredienteService.listarBajoStock());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BODEGUERO', 'COCINA')")
    public ResponseEntity<IngredienteResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ingredienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BODEGUERO')")
    public ResponseEntity<IngredienteResponseDTO> actualizarIngrediente(
            @PathVariable Integer id,
            @Valid @RequestBody IngredienteRequestDTO requestDTO) {
        return ResponseEntity.ok(ingredienteService.actualizarIngrediente(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BODEGUERO')")
    public ResponseEntity<Void> eliminarIngrediente(@PathVariable Integer id) {
        ingredienteService.eliminarIngrediente(id);
        return ResponseEntity.noContent().build();
    }
}