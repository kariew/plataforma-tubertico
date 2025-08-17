// src/main/java/com/tubertico/dto/CotizacionDto.java
package com.tubertico.dto;

import java.util.ArrayList;
import java.util.List;

public class CotizacionDto {
        private Long usuarioId;
        private String naviera;
        private String comentarios;

        // Aquí la lista de líneas
        private List<CotizacionItemDto> items = new ArrayList<>();

        // getters y setters para usuarioId, naviera, comentarios...
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

        public String getNaviera() { return naviera; }
        public void setNaviera(String naviera) { this.naviera = naviera; }

        public String getComentarios() { return comentarios; }
        public void setComentarios(String comentarios) { this.comentarios = comentarios; }

        // getters/setters para la lista
        public List<CotizacionItemDto> getItems() { return items; }
        public void setItems(List<CotizacionItemDto> items) { this.items = items; }
}
