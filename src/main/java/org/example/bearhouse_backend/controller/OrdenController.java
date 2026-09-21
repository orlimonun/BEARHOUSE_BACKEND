package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.orden.OrdenRequestDTO;
import org.example.bearhouse_backend.dto.orden.OrdenResponseDTO;
import org.example.bearhouse_backend.dto.pago.PagoResponseDTO;
import org.example.bearhouse_backend.service.OrdenService;
import org.example.bearhouse_backend.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;
    private final PagoService pagoService;

    public OrdenController(OrdenService ordenService, PagoService pagoService) {
        this.ordenService = ordenService;
        this.pagoService = pagoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<OrdenResponseDTO> crear(@Valid @RequestBody OrdenRequestDTO requestDTO) {
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

    // "Todo junto": cobra todo lo pendiente en un solo pago
    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<PagoResponseDTO> pagar(
            @PathVariable Integer id,
            @RequestParam(required = false) String metodoPago) {
        String metodo = (metodoPago != null && !metodoPago.isBlank()) ? metodoPago : "EFECTIVO";
        return ResponseEntity.ok(pagoService.pagarTodo(id, metodo));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'MESERO')")
    public ResponseEntity<OrdenResponseDTO> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(ordenService.cancelarOrden(id));
    }
}