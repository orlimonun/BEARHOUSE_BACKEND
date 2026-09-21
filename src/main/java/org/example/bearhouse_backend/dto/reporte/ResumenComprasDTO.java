package org.example.bearhouse_backend.dto.reporte;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ResumenComprasDTO {
    private LocalDateTime desde;
    private LocalDateTime hasta;
    private Long totalCompras;
    private BigDecimal totalGastado;
    private BigDecimal promedioPorCompra;
    private List<GastoPorProveedorDTO> gastoPorProveedor;

    public LocalDateTime getDesde() {
        return desde;
    }

    public void setDesde(LocalDateTime desde) {
        this.desde = desde;
    }

    public LocalDateTime getHasta() {
        return hasta;
    }

    public void setHasta(LocalDateTime hasta) {
        this.hasta = hasta;
    }

    public Long getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(Long totalCompras) {
        this.totalCompras = totalCompras;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }

    public BigDecimal getPromedioPorCompra() {
        return promedioPorCompra;
    }

    public void setPromedioPorCompra(BigDecimal promedioPorCompra) {
        this.promedioPorCompra = promedioPorCompra;
    }

    public List<GastoPorProveedorDTO> getGastoPorProveedor() {
        return gastoPorProveedor;
    }

    public void setGastoPorProveedor(List<GastoPorProveedorDTO> gastoPorProveedor) {
        this.gastoPorProveedor = gastoPorProveedor;
    }

    // Getters y setters
}