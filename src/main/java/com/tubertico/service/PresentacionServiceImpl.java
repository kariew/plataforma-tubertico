package com.tubertico.service;

import com.tubertico.model.Presentacion;
import com.tubertico.repository.PresentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresentacionServiceImpl implements PresentacionService {

    @Autowired
    private PresentacionRepository presentacionRepository;

    @Override
    public List<Presentacion> findAll() {
        return presentacionRepository.findAll();
    }

    @Override
    public List<Presentacion> findByProductoId(Long productoId) {
        return presentacionRepository.findByProductoId(productoId);
    }
}
