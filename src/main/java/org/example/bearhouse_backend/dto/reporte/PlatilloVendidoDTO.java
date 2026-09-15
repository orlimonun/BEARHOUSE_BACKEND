package org.example.bearhouse_backend.dto.reporte;

public class PlatilloVendidoDTO {

    private String platilloNombre;
    private Long cantidadVendida;

    public PlatilloVendidoDTO(String platilloNombre, Long cantidadVendida) {
        this.platilloNombre = platilloNombre;
        this.cantidadVendida = cantidadVendida;
    }

    public String getPlatilloNombre() {
        return platilloNombre;
    }

    public void setPlatilloNombre(String platilloNombre) {
        this.platilloNombre = platilloNombre;
    }

    public Long getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(Long cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }
}