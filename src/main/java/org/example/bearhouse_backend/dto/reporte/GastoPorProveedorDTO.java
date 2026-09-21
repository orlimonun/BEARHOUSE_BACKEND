package org.example.bearhouse_backend.dto.reporte;

import java.math.BigDecimal;

public class GastoPorProveedorDTO {
    private String proveedorNombre;
    private Long cantidadCompras;
    private BigDecimal totalGastado;

    public GastoPorProveedorDTO(String proveedorNombre, Long cantidadCompras, BigDecimal totalGastado) {
        this.proveedorNombre = proveedorNombre;
        this.cantidadCompras = cantidadCompras;
        this.totalGastado = totalGastado;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public Long getCantidadCompras() {
        return cantidadCompras;
    }

    public void setCantidadCompras(Long cantidadCompras) {
        this.cantidadCompras = cantidadCompras;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }
}