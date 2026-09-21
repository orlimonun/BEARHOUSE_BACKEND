package org.example.bearhouse_backend.dto.reporte;

import java.math.BigDecimal;

public class IngredienteCompradoDTO {
    private String ingredienteNombre;
    private String unidadMedida;
    private BigDecimal cantidadTotal;
    private BigDecimal totalGastado;

    public IngredienteCompradoDTO(String ingredienteNombre, String unidadMedida,
                                  BigDecimal cantidadTotal, BigDecimal totalGastado) {
        this.ingredienteNombre = ingredienteNombre;
        this.unidadMedida = unidadMedida;
        this.cantidadTotal = cantidadTotal;
        this.totalGastado = totalGastado;
    }

    public String getIngredienteNombre() {
        return ingredienteNombre;
    }

    public void setIngredienteNombre(String ingredienteNombre) {
        this.ingredienteNombre = ingredienteNombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(BigDecimal cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }
    // Getters y setters
}