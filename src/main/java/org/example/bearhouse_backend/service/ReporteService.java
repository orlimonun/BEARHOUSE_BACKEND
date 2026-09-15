package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.reporte.PlatilloVendidoDTO;
import org.example.bearhouse_backend.dto.reporte.ResumenGeneralDTO;
import org.example.bearhouse_backend.dto.reporte.VentasPorPeriodoDTO;
import org.example.bearhouse_backend.dto.reporte.VentasPorTipoDTO;
import org.example.bearhouse_backend.repository.OrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteService {

    private final OrdenRepository ordenRepository;

    public ReporteService(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @Transactional(readOnly = true)
    public VentasPorPeriodoDTO ventasPorPeriodo(LocalDateTime desde, LocalDateTime hasta) {
        BigDecimal total = ordenRepository.calcularVentasTotales(desde, hasta);
        Long cantidad = ordenRepository.contarOrdenesPagadas(desde, hasta);


        BigDecimal ticketPromedio = BigDecimal.ZERO;
        if (cantidad != null && cantidad > 0) {
            ticketPromedio = total.divide(
                    new BigDecimal(cantidad), 2, RoundingMode.HALF_UP);
        }

        VentasPorPeriodoDTO dto = new VentasPorPeriodoDTO();
        dto.setDesde(desde);
        dto.setHasta(hasta);
        dto.setTotalFacturado(total);
        dto.setCantidadOrdenes(cantidad);
        dto.setTicketPromedio(ticketPromedio);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<PlatilloVendidoDTO> platillosMasVendidos(LocalDateTime desde, LocalDateTime hasta) {
        List<Object[]> resultados = ordenRepository.platillosMasVendidos(desde, hasta);

        List<PlatilloVendidoDTO> ranking = new ArrayList<>();
        for (Object[] fila : resultados) {
            String nombre = (String) fila[0];
            Long cantidad = (Long) fila[1];
            ranking.add(new PlatilloVendidoDTO(nombre, cantidad));
        }
        return ranking;
    }

    @Transactional(readOnly = true)
    public ResumenGeneralDTO resumenGeneral(LocalDateTime desde, LocalDateTime hasta) {

        BigDecimal totalFacturado = ordenRepository.calcularVentasTotales(desde, hasta);
        Long totalOrdenes = ordenRepository.contarOrdenesPagadas(desde, hasta);

        BigDecimal ticketPromedio = BigDecimal.ZERO;
        if (totalOrdenes != null && totalOrdenes > 0) {
            ticketPromedio = totalFacturado.divide(
                    new BigDecimal(totalOrdenes), 2, RoundingMode.HALF_UP);
        }


        List<Object[]> resultadosTipo = ordenRepository.ventasPorTipo(desde, hasta);
        List<VentasPorTipoDTO> desglose = new ArrayList<>();
        for (Object[] fila : resultadosTipo) {
            String tipo = fila[0].toString();
            Long cantidad = (Long) fila[1];
            BigDecimal total = (BigDecimal) fila[2];
            desglose.add(new VentasPorTipoDTO(tipo, cantidad, total));
        }

        ResumenGeneralDTO dto = new ResumenGeneralDTO();
        dto.setDesde(desde);
        dto.setHasta(hasta);
        dto.setTotalOrdenes(totalOrdenes);
        dto.setTotalFacturado(totalFacturado);
        dto.setTicketPromedio(ticketPromedio);
        dto.setDesglosePorTipo(desglose);
        return dto;
    }

}