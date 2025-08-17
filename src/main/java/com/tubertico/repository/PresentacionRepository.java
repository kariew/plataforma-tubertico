package com.tubertico.repository;

import com.tubertico.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PresentacionRepository extends JpaRepository<Presentacion, Long> {
    List<Presentacion> findByProductoId(Long productoId);
}
