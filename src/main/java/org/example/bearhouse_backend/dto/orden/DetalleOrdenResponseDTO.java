package org.example.bearhouse_backend.dto.orden;

import java.math.BigDecimal;

public class DetalleOrdenResponseDTO {

    private Integer id;
    private Integer platilloId;
    private String platilloNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private Integer cantidadPagada;
    private Integer cantidadPendiente;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getCantidadPagada() {
        return cantidadPagada;
    }

    public void setCantidadPagada(Integer cantidadPagada) {
        this.cantidadPagada = cantidadPagada;
    }

    public Integer getCantidadPendiente() {
        return cantidadPendiente;
    }

    public void setCantidadPendiente(Integer cantidadPendiente) {
        this.cantidadPendiente = cantidadPendiente;
    }
}