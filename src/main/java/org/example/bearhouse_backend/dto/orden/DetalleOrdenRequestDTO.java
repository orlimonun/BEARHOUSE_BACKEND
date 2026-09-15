package org.example.bearhouse_backend.dto.orden;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetalleOrdenRequestDTO {

    @NotNull(message = "El platillo es obligatorio")
    private Integer platilloId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    public Integer getPlatilloId() {
        return platilloId;
    }

    public void setPlatilloId(Integer platilloId) {
        this.platilloId = platilloId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }


}