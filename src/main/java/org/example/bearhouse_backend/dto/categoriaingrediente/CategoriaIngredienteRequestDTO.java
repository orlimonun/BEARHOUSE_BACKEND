package org.example.bearhouse_backend.dto.categoriaingrediente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaIngredienteRequestDTO {

    @NotBlank(message = "EL nombre de la categoria debe ser obligatoria")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
