package org.example.bearhouse_backend.dto.reporte;

import java.math.BigDecimal;

public class VentasPorTipoDTO {

    private String tipo;
    private Long cantidadOrdenes;
    private BigDecimal totalFacturado;

    public VentasPorTipoDTO(String tipo, Long cantidadOrdenes, BigDecimal totalFacturado) {
        this.tipo = tipo;
        this.cantidadOrdenes = cantidadOrdenes;
        this.totalFacturado = totalFacturado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getCantidadOrdenes() {
        return cantidadOrdenes;
    }

    public void setCantidadOrdenes(Long cantidadOrdenes) {
        this.cantidadOrdenes = cantidadOrdenes;
    }

    public BigDecimal getTotalFacturado() {
        return totalFacturado;
    }

    public void setTotalFacturado(BigDecimal totalFacturado) {
        this.totalFacturado = totalFacturado;
    }
}