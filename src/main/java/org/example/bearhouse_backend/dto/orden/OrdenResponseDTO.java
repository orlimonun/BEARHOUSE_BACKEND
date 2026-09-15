package org.example.bearhouse_backend.dto.orden;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenResponseDTO {

    private Integer id;
    private String tipo;
    private String estado;
    private String metodoPago;
    private Integer mesaId;
    private Integer mesaNumero;
    private Integer empleadoId;
    private String empleadoNombre;
    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal transporte;
    private BigDecimal kilometros;
    private BigDecimal total;
    private String observaciones;
    private List<DetalleOrdenResponseDTO> detalles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Integer getMesaId() {
        return mesaId;
    }

    public void setMesaId(Integer mesaId) {
        this.mesaId = mesaId;
    }

    public Integer getMesaNumero() {
        return mesaNumero;
    }

    public void setMesaNumero(Integer mesaNumero) {
        this.mesaNumero = mesaNumero;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getTransporte() {
        return transporte;
    }

    public void setTransporte(BigDecimal transporte) {
        this.transporte = transporte;
    }

    public BigDecimal getKilometros() {
        return kilometros;
    }

    public void setKilometros(BigDecimal kilometros) {
        this.kilometros = kilometros;
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

    public List<DetalleOrdenResponseDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenResponseDTO> detalles) {
        this.detalles = detalles;
    }
}