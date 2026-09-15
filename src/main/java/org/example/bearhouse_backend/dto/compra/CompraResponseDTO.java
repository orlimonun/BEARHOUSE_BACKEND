package org.example.bearhouse_backend.dto.compra;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class CompraResponseDTO {

    private Integer id;
    private Integer proveedorId;
    private String proveedorNombre;
    private Integer empleadoId;
    private String empleadoNombre;
    private LocalDateTime fecha;
    private String numFactura;
    private BigDecimal total;
    private String observaciones;
    private List<DetalleCompraResponseDTO> detalles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getEmpleadoNombre() {
        return empleadoNombre;
    }

    public void setEmpleadoNombre(String empleadoNombre) {
        this.empleadoNombre = empleadoNombre;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetalleCompraResponseDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraResponseDTO> detalles) {
        this.detalles = detalles;
    }
}
