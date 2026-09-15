package org.example.bearhouse_backend.controller;

import org.example.bearhouse_backend.dto.reporte.PlatilloVendidoDTO;
import org.example.bearhouse_backend.dto.reporte.ResumenGeneralDTO;
import org.example.bearhouse_backend.dto.reporte.VentasPorPeriodoDTO;
import org.example.bearhouse_backend.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@PreAuthorize("hasRole('ADMIN')")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/ventas-por-periodo")
    public ResponseEntity<VentasPorPeriodoDTO> ventasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(reporteService.ventasPorPeriodo(desde, hasta));
    }

    @GetMapping("/platillos-mas-vendidos")
    public ResponseEntity<List<PlatilloVendidoDTO>> platillosMasVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(reporteService.platillosMasVendidos(desde, hasta));
    }

    @GetMapping("/resumen-general")
    public ResponseEntity<ResumenGeneralDTO> resumenGeneral(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(reporteService.resumenGeneral(desde, hasta));
    }

}