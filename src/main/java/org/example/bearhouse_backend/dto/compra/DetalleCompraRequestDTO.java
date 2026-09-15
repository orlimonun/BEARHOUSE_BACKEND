package org.example.bearhouse_backend.dto.compra;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DetalleCompraRequestDTO {

    @NotNull(message = "EL ingrediente es obligatorio")
    private Integer ingredienteId;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.001" , message = "La cantidad debe ser mayor a cero")
    private BigDecimal cantidad;

    @NotNull(message = "El costo Unitario es obligatorio")
    @DecimalMin(value = "0.001", message = "El costo no puede ser negativo")
    private BigDecimal costoUnitario;

    public Integer getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteID(Integer ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }
}
