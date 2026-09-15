package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.categoriaplatillo.CategoriaPlatilloRequestDTO;
import org.example.bearhouse_backend.dto.categoriaplatillo.CategoriaPlatilloResponseDTO;
import org.example.bearhouse_backend.service.CategoriaPlatilloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-platillo")
public class CategoriaPlatilloController {

    private final CategoriaPlatilloService categoriaService;

    public CategoriaPlatilloController(CategoriaPlatilloService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaPlatilloResponseDTO> crear(
            @Valid @RequestBody CategoriaPlatilloRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(requestDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA', 'BODEGUERO')")
    public ResponseEntity<List<CategoriaPlatilloResponseDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA', 'BODEGUERO')")
    public ResponseEntity<CategoriaPlatilloResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaPlatilloResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaPlatilloRequestDTO requestDTO) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
