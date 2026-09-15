package org.example.bearhouse_backend.dto.mesa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MesaRequestDTO {

    @NotNull(message = "El número de mesa es obligatorio")
    @Min(value = 1, message = "El número de mesa debe ser positivo")
    private Integer numero;

    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidad;   // opcional

    private String estado;   // opcional: LIBRE, OCUPADA, RESERVADA (si no viene, arranca LIBRE)

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}