package com.tubertico.service;

import com.tubertico.dto.CotizacionDto;
import com.tubertico.dto.CotizacionItemDto;
import com.tubertico.model.*;
import com.tubertico.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CotizacionService {

    @Autowired private CotizacionRepository        cotizacionRepository;
    @Autowired private CotizacionItemRepository    cotizacionItemRepository;
    @Autowired private UsuarioRepository           usuarioRepository;
    @Autowired private PresentacionRepository      presentacionRepository;
    @Autowired private ProductoRepository          productoRepository;

    @Transactional
    public Cotizacion crearYCargarCotizacion(CotizacionDto dto) {
        // 1) Usuario
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + dto.getUsuarioId()));

        // 2) Header de la cotización
        Cotizacion cot = new Cotizacion();
        cot.setUsuario(usuario);
        cot.setNaviera(dto.getNaviera());
        cot.setComentarios(dto.getComentarios());
        cot = cotizacionRepository.save(cot);

        // 3) Por cada línea en el DTO, crea un CotizacionItem
        for (CotizacionItemDto linea : dto.getItems()) {

            // a) Presentación
            Presentacion pres = presentacionRepository.findById(linea.getPresentacionId())
                    .orElseThrow(() -> new IllegalArgumentException("Presentación no encontrada: " + linea.getPresentacionId()));

            // b) Producto
            Producto prod = productoRepository.findById(linea.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + linea.getProductoId()));

            // c) Item
            CotizacionItem item = new CotizacionItem();
            item.setCotizacion(cot);
            item.setPresentacion(pres);
            item.setProducto(prod);
            item.setUnidades(linea.getUnidades());
            item.setPesoEstimado(linea.getPesoEstimado());
            cotizacionItemRepository.save(item);
        }

        return cot;
    }

    public List<Cotizacion> findAll() {
        return cotizacionRepository.findAll();
    }
}