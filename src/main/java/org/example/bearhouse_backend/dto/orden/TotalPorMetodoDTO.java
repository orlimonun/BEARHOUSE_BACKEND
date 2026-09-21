package org.example.bearhouse_backend.dto.orden;

import java.math.BigDecimal;

public class TotalPorMetodoDTO {

    private String metodoPago;
    private BigDecimal monto;

    public TotalPorMetodoDTO(String metodoPago, BigDecimal monto) {
        this.metodoPago = metodoPago;
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

}