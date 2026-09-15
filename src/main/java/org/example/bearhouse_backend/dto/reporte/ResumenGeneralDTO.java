package org.example.bearhouse_backend.dto.reporte;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ResumenGeneralDTO {

    private LocalDateTime desde;
    private LocalDateTime hasta;
    private Long totalOrdenes;
    private BigDecimal totalFacturado;
    private BigDecimal ticketPromedio;
    private List<VentasPorTipoDTO> desglosePorTipo;

    public LocalDateTime getDesde() {
        return desde;
    }

    public void setDesde(LocalDateTime desde) {
        this.desde = desde;
    }

    public LocalDateTime getHasta() {
        return hasta;
    }

    public void setHasta(LocalDateTime hasta) {
        this.hasta = hasta;
    }

    public Long getTotalOrdenes() {
        return totalOrdenes;
    }

    public void setTotalOrdenes(Long totalOrdenes) {
        this.totalOrdenes = totalOrdenes;
    }

    public BigDecimal getTotalFacturado() {
        return totalFacturado;
    }

    public void setTotalFacturado(BigDecimal totalFacturado) {
        this.totalFacturado = totalFacturado;
    }

    public BigDecimal getTicketPromedio() {
        return ticketPromedio;
    }

    public void setTicketPromedio(BigDecimal ticketPromedio) {
        this.ticketPromedio = ticketPromedio;
    }

    public List<VentasPorTipoDTO> getDesglosePorTipo() {
        return desglosePorTipo;
    }

    public void setDesglosePorTipo(List<VentasPorTipoDTO> desglosePorTipo) {
        this.desglosePorTipo = desglosePorTipo;
    }
}