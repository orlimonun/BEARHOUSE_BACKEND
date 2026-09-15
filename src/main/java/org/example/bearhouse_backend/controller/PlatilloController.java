package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.platillo.PlatilloRequestDTO;
import org.example.bearhouse_backend.dto.platillo.PlatilloResponseDTO;
import org.example.bearhouse_backend.service.PlatilloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platillos")
public class PlatilloController {

    private final PlatilloService platilloService;

    public PlatilloController(PlatilloService platilloService) {
        this.platilloService = platilloService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlatilloResponseDTO> crear(
            @Valid @RequestBody PlatilloRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(platilloService.crearPlatillo(requestDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA', 'BODEGUERO')")
    public ResponseEntity<List<PlatilloResponseDTO>> listar() {
        return ResponseEntity.ok(platilloService.listarPlatillos());
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA', 'BODEGUERO')")
    public ResponseEntity<List<PlatilloResponseDTO>> listarDisponibles() {
        return ResponseEntity.ok(platilloService.listarDisponibles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA', 'BODEGUERO')")
    public ResponseEntity<PlatilloResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(platilloService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlatilloResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PlatilloRequestDTO requestDTO) {
        return ResponseEntity.ok(platilloService.actualizarPlatillo(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        platilloService.eliminarPlatillo(id);
        return ResponseEntity.noContent().build();
    }
}