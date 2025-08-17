package com.tubertico.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cotizacion_item")
public class CotizacionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cabecera
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotizacion_id", nullable = false)
    private Cotizacion cotizacion;

    // Presentación seleccionada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presentacion_id", nullable = false)
    private Presentacion presentacion;

    // Producto seleccionado
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Datos del detalle
    @Column(name = "unidades", nullable = false)
    private Integer unidades;

    @Column(name = "peso_estimado", nullable = false)
    private Double pesoEstimado;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public Presentacion getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public Double getPesoEstimado() {
        return pesoEstimado;
    }

    public void setPesoEstimado(Double pesoEstimado) {
        this.pesoEstimado = pesoEstimado;
    }
}
