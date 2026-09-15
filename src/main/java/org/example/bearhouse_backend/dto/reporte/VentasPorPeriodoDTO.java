package org.example.bearhouse_backend.dto.reporte;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentasPorPeriodoDTO {

    private LocalDateTime desde;
    private LocalDateTime hasta;
    private BigDecimal totalFacturado;
    private Long cantidadOrdenes;
    private BigDecimal ticketPromedio;

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

    public BigDecimal getTotalFacturado() {
        return totalFacturado;
    }

    public void setTotalFacturado(BigDecimal totalFacturado) {
        this.totalFacturado = totalFacturado;
    }

    public Long getCantidadOrdenes() {
        return cantidadOrdenes;
    }

    public void setCantidadOrdenes(Long cantidadOrdenes) {
        this.cantidadOrdenes = cantidadOrdenes;
    }

    public BigDecimal getTicketPromedio() {
        return ticketPromedio;
    }

    public void setTicketPromedio(BigDecimal ticketPromedio) {
        this.ticketPromedio = ticketPromedio;
    }
}