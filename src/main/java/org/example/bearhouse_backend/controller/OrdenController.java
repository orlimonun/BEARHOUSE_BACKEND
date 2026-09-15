package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.orden.OrdenRequestDTO;
import org.example.bearhouse_backend.dto.orden.OrdenResponseDTO;
import org.example.bearhouse_backend.service.OrdenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<OrdenResponseDTO> crear(
            @Valid @RequestBody OrdenRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenService.crearOrden(requestDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA')")
    public ResponseEntity<List<OrdenResponseDTO>> listar() {
        return ResponseEntity.ok(ordenService.listarOrdenes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO', 'COCINA')")
    public ResponseEntity<OrdenResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    // Marcar una orden como pagada
    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<OrdenResponseDTO> pagar(
            @PathVariable Integer id,
            @RequestParam(required = false) String metodoPago) {
        return ResponseEntity.ok(ordenService.pagarOrden(id, metodoPago));
    }

    // Cancelar una orden (devuelve stock)
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<OrdenResponseDTO> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(ordenService.cancelarOrden(id));
    }
}