package org.example.bearhouse_backend.dto.pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PagoResponseDTO {

    private Integer id;
    private Integer ordenId;
    private BigDecimal monto;
    private BigDecimal transporteCobrado;
    private String metodoPago;
    private String empleadoNombre;
    private LocalDateTime fecha;
    private List<DetallePagoResponseDTO> detalles;

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Integer ordenId) {
        this.ordenId = ordenId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getTransporteCobrado() {
        return transporteCobrado;
    }

    public void setTransporteCobrado(BigDecimal transporteCobrado) {
        this.transporteCobrado = transporteCobrado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
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

    public List<DetallePagoResponseDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePagoResponseDTO> detalles) {
        this.detalles = detalles;
    }
}