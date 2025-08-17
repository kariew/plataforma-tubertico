package com.tubertico.service;

import com.tubertico.model.Presentacion;
import java.util.List;

public interface PresentacionService {
    List<Presentacion> findAll();
    List<Presentacion> findByProductoId(Long productoId);
}