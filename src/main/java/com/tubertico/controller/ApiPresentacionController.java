package com.tubertico.controller;

import com.tubertico.model.Presentacion;
import com.tubertico.service.PresentacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API para consultar presentaciones por producto.
 * GET /api/presentaciones?productoId=123  ->  JSON [{id, tipo, pesoEstimado}]
 *
 * Nota: devolvemos un DTO plano para evitar problemas de serialización
 * con relaciones LAZY o ciclos entre entidades.
 */
@RestController
@RequestMapping("/api")
public class ApiPresentacionController {

    private static final Logger log = LoggerFactory.getLogger(ApiPresentacionController.class);

    private final PresentacionService presentacionService;

    public ApiPresentacionController(PresentacionService presentacionService) {
        this.presentacionService = presentacionService;
    }

    // DTO liviano para el front
    public record PresentacionDto(Long id, String tipo, String pesoEstimado) {}

    @GetMapping(value = "/presentaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PresentacionDto>> getByProducto(@RequestParam("productoId") Long productoId) {
        log.debug("Consultando presentaciones para productoId={}", productoId);

        // ProductoId nulo -> 400
        if (productoId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Trae entidades y mapea a DTOs seguros para JSON
        List<PresentacionDto> dtos = presentacionService.findByProductoId(productoId)
                .stream()
                .map(p -> new PresentacionDto(
                        p.getId(),
                        p.getTipo(),
                        // lo mandamos como String para evitar problemas de tipo (BigDecimal/Double)
                        p.getPesoEstimado() == null ? null : String.valueOf(p.getPesoEstimado())
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleAny(Exception ex) {
        log.error("Error en /api/presentaciones", ex);
        return ResponseEntity.internalServerError().build();
    }
}
