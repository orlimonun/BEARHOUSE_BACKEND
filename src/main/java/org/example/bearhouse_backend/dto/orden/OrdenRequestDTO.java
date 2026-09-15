package org.example.bearhouse_backend.dto.orden;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class OrdenRequestDTO {

    @NotNull(message = "El tipo de orden es obligatorio")
    private String tipo;

    private Integer mesaId;       // (solo LOCAL)
    private Integer empleadoId;

    private String metodoPago;

    // Solo para órdenes EXPRESS
    @DecimalMin(value = "0.0", message = "Los kilómetros no pueden ser negativos")
    private BigDecimal kilometros;

    @Size(max = 300, message = "Las observaciones no pueden superar los 300 caracteres")
    private String observaciones;

    @NotEmpty(message = "La orden debe tener al menos un platillo")
    @Valid
    private List<DetalleOrdenRequestDTO> detalles;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getMesaId() {
        return mesaId;
    }

    public void setMesaId(Integer mesaId) {
        this.mesaId = mesaId;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getKilometros() {
        return kilometros;
    }

    public void setKilometros(BigDecimal kilometros) {
        this.kilometros = kilometros;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetalleOrdenRequestDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenRequestDTO> detalles) {
        this.detalles = detalles;
    }
}