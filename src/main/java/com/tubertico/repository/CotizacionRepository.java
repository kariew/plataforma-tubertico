package com.tubertico.repository;

import com.tubertico.model.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    // Aquí puedes añadir métodos personalizados, p.ej. findByUsuarioId(...)
}
