package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.pago.PagoRequestDTO;
import org.example.bearhouse_backend.dto.pago.PagoResponseDTO;
import org.example.bearhouse_backend.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes/{ordenId}/pagos")
@PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrarPago(
            @PathVariable Integer ordenId,
            @Valid @RequestBody PagoRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.registrarPago(ordenId, requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listarPagos(@PathVariable Integer ordenId) {
        return ResponseEntity.ok(pagoService.listarPagosDeOrden(ordenId));
    }
}