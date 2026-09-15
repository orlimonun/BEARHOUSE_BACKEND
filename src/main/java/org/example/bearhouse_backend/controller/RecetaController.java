package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.receta.RecetaRequestDTO;
import org.example.bearhouse_backend.dto.receta.RecetaResponseDTO;
import org.example.bearhouse_backend.service.RecetaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    // Agregar un ingrediente a la receta de un platillo
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecetaResponseDTO> agregarIngrediente(
            @Valid @RequestBody RecetaRequestDTO requestDTO) {
        RecetaResponseDTO creada = recetaService.agregarIngrediente(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // Ver la receta completa de un platillo
    @GetMapping("/platillo/{platilloId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COCINA')")
    public ResponseEntity<List<RecetaResponseDTO>> verRecetaDePlatillo(
            @PathVariable Integer platilloId) {
        return ResponseEntity.ok(recetaService.verRecetaDePlatillo(platilloId));
    }

    // Actualizar una línea de receta
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecetaResponseDTO> actualizarCantidad(
            @PathVariable Integer id,
            @Valid @RequestBody RecetaRequestDTO requestDTO) {
        return ResponseEntity.ok(recetaService.actualizarCantidad(id, requestDTO));
    }

    // Quitar una línea de receta
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> quitarIngrediente(@PathVariable Integer id) {
        recetaService.quitarIngrediente(id);
        return ResponseEntity.noContent().build();
    }
}