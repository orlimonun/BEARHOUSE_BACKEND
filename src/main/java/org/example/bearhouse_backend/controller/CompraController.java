package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.compra.CompraRequestDTO;
import org.example.bearhouse_backend.dto.compra.CompraResponseDTO;
import org.example.bearhouse_backend.service.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@PreAuthorize("hasAnyRole('ADMIN', 'BODEGUERO')")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraResponseDTO> crearCompra(
            @Valid @RequestBody CompraRequestDTO requestDTO) {
        CompraResponseDTO creada = compraService.crearCompra(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> listarCompras() {
        return ResponseEntity.ok(compraService.listarCompras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(compraService.buscarPorId(id));
    }
}
