package org.example.bearhouse_backend.dto.compra;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CompraRequestDTO {

    @NotNull(message = "El proveedor es obligatorio")
    private Integer proveedorId;

    private Integer empleadoId;   // opcional

    @Size(max = 60, message = "El número de factura no puede superar los 60 caracteres")
    private String numFactura;

    @Size(max = 300, message = "Las observaciones no pueden superar los 300 caracteres")
    private String observaciones;

    @NotEmpty(message = "La compra debe tener al menos un detalle")
    @Valid
    private List<DetalleCompraRequestDTO> detalles;

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetalleCompraRequestDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraRequestDTO> detalles) {
        this.detalles = detalles;
    }
}