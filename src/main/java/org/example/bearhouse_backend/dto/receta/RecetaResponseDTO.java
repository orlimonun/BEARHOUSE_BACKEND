package org.example.bearhouse_backend.dto.receta;

import java.math.BigDecimal;

public class RecetaResponseDTO {

    private Integer id;
    private Integer platilloId;
    private String platilloNombre;
    private Integer ingredienteId;
    private String ingredienteNombre;
    private String unidadMedida;
    private BigDecimal cantidad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlatilloId() {
        return platilloId;
    }

    public void setPlatilloId(Integer platilloId) {
        this.platilloId = platilloId;
    }

    public String getPlatilloNombre() {
        return platilloNombre;
    }

    public void setPlatilloNombre(String platilloNombre) {
        this.platilloNombre = platilloNombre;
    }

    public Integer getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Integer ingredienteId) {
        this.ingredienteId = ingredienteId;
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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
}
