package org.example.bearhouse_backend.dto.ingrediente;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class IngredienteRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre del ingrediente no puede superar los 120 caracteres")
    private String nombre;

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Size(max = 20,  message = "La unidad de medida no puede superar los 20 caracteres")
    private String unidadMedida;

    @DecimalMin(value = "0.0", message = "El stock minimo no puede ser negativo")
    private BigDecimal stockMinimo;

    @DecimalMin(value = "0.0", message = "El costo unitario no puede ser negativo")
    private BigDecimal costoUnitario;

    private Integer categoriaId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }
}
