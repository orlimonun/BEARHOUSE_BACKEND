package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.categoriaingrediente.CategoriaIngredienteRequestDTO;
import org.example.bearhouse_backend.dto.categoriaingrediente.CategoriaIngredienteResponseDTO;
import org.example.bearhouse_backend.service.CategoriaIngredienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-ingrediente")
public class CategoriaIngredienteController {

    private final CategoriaIngredienteService categoriaService;

    public CategoriaIngredienteController(CategoriaIngredienteService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaIngredienteResponseDTO> crearCategoria(
            @Valid @RequestBody CategoriaIngredienteRequestDTO requestDTO) {
        CategoriaIngredienteResponseDTO creada = categoriaService.crearCategoria(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA', 'BODEGUERO')")
    public ResponseEntity<List<CategoriaIngredienteResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA', 'BODEGUERO')")
    public ResponseEntity<CategoriaIngredienteResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaIngredienteResponseDTO> actualizarCategoria(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaIngredienteRequestDTO requestDTO) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
