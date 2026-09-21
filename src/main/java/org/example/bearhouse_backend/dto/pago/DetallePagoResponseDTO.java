package org.example.bearhouse_backend.dto.pago;

import java.math.BigDecimal;

public class DetallePagoResponseDTO {

    private Integer detalleOrdenId;
    private String platilloNombre;
    private Integer cantidad;
    private BigDecimal subtotal;

    public Integer getDetalleOrdenId() {
        return detalleOrdenId;
    }

    public void setDetalleOrdenId(Integer detalleOrdenId) {
        this.detalleOrdenId = detalleOrdenId;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    // Getters y setters
}