// src/main/java/com/tubertico/dto/CotizacionItemDto.java
package com.tubertico.dto;

public class CotizacionItemDto {
    private Long productoId;
    private Long presentacionId;
    private Integer unidades;
    private Double pesoEstimado;

    // getters y setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Long getPresentacionId() { return presentacionId; }
    public void setPresentacionId(Long presentacionId) { this.presentacionId = presentacionId; }

    public Integer getUnidades() { return unidades; }
    public void setUnidades(Integer unidades) { this.unidades = unidades; }

    public Double getPesoEstimado() { return pesoEstimado; }
    public void setPesoEstimado(Double pesoEstimado) { this.pesoEstimado = pesoEstimado; }
}
