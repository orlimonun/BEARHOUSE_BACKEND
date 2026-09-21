package org.example.bearhouse_backend.dto.pago;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class PagoRequestDTO {

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @NotEmpty(message = "Seleccioná al menos un ítem a pagar")
    @Valid
    private List<ItemPagoRequestDTO> items;

    // Getters y setters

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<ItemPagoRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemPagoRequestDTO> items) {
        this.items = items;
    }
}