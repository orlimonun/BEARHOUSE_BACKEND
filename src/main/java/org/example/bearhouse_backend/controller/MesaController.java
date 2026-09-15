package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.mesa.MesaRequestDTO;
import org.example.bearhouse_backend.dto.mesa.MesaResponseDTO;
import org.example.bearhouse_backend.service.MesaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MesaResponseDTO> crear(@Valid @RequestBody MesaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaService.crearMesa(requestDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<List<MesaResponseDTO>> listar() {
        return ResponseEntity.ok(mesaService.listarMesas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<MesaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(mesaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MesaResponseDTO> actualizar(
            @PathVariable Integer id, @Valid @RequestBody MesaRequestDTO requestDTO) {
        return ResponseEntity.ok(mesaService.actualizarMesa(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        mesaService.eliminarMesa(id);
        return ResponseEntity.noContent().build();
    }
}