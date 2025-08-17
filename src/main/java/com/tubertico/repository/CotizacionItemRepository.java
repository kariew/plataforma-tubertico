package com.tubertico.repository;

import com.tubertico.model.CotizacionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CotizacionItemRepository extends JpaRepository<CotizacionItem, Long> {
    // Métodos extra si los necesitas
}
