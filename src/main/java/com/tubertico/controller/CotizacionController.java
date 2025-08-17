// src/main/java/com/tubertico/controller/CotizacionController.java
package com.tubertico.controller;

import com.tubertico.dto.CotizacionDto;
import com.tubertico.dto.CotizacionItemDto;
import com.tubertico.model.Producto;
import com.tubertico.model.Presentacion;
import com.tubertico.service.CotizacionService;
import com.tubertico.service.ProductoService;
import com.tubertico.service.UsuarioService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CotizacionController {

    @Autowired private ProductoService productoService;
    @Autowired private CotizacionService cotizacionService;
    @Autowired private UsuarioService usuarioService;

    @GetMapping("/formulario")
    public String mostrarFormulario(Model model, Principal principal) {
        // DTO base: 1 fila vacía, sin producto preseleccionado
        CotizacionDto dto = new CotizacionDto();
        dto.setUsuarioId(1L); // o desde principal
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            dto.setItems(new ArrayList<>());
            dto.getItems().add(new CotizacionItemDto()); // todo null/0: producto sin seleccionar
        }

        // Productos siempre
        List<Producto> productos = productoService.listarTodos();

        // Presentaciones vacías (las cargará el JS vía /api/presentaciones al elegir producto)
        List<Presentacion> presentaciones = List.of();

        // calcula si el usuario es ADMIN y lo manda al modelo
        boolean isAdmin = false;
        if (principal != null) {
            var u = usuarioService.buscarPorCorreo(principal.getName());
            if (u != null && "ADMIN".equalsIgnoreCase(u.getRol())) {
                isAdmin = true;
            }
        }

        model.addAttribute("cotizacionDto", dto);
        model.addAttribute("productos", productos);
        model.addAttribute("presentaciones", presentaciones);
        model.addAttribute("isAdmin", isAdmin);


        return "formulario";
    }

    @PostMapping("/cotizaciones")
    public String procesarCotizacion(@ModelAttribute("cotizacionDto") CotizacionDto dto,
                                     RedirectAttributes ra) {
        cotizacionService.crearYCargarCotizacion(dto);

        // Flash attribute para que el modal se muestre en /formulario
        ra.addFlashAttribute("cotizacionEnviada", true);

        // PRG
        return "redirect:/formulario";
    }

    @GetMapping("/formulario_en")
    public String mostrarFormularioEn(Model model, Principal principal) {
        // DTO base: 1 fila vacía, sin producto preseleccionado
        CotizacionDto dto = new CotizacionDto();
        dto.setUsuarioId(1L); // o desde principal
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            dto.setItems(new ArrayList<>());
            dto.getItems().add(new CotizacionItemDto()); // todo null/0: producto sin seleccionar
        }

        // Productos siempre
        List<Producto> productos = productoService.listarTodos();

        // Presentaciones vacías (las cargará el JS vía /api/presentaciones al elegir producto)
        List<Presentacion> presentaciones = List.of();

        // calcula si el usuario es ADMIN y lo manda al modelo
        boolean isAdmin = false;
        if (principal != null) {
            var u = usuarioService.buscarPorCorreo(principal.getName());
            if (u != null && "ADMIN".equalsIgnoreCase(u.getRol())) {
                isAdmin = true;
            }
        }

        model.addAttribute("cotizacionDto", dto);
        model.addAttribute("productos", productos);
        model.addAttribute("presentaciones", presentaciones);
        model.addAttribute("isAdmin", isAdmin);

        // NO es necesario setear aquí "cotizacionEnviada":
        // si viene por FlashAttribute desde el redirect, Thymeleaf lo ve directo.

        return "formulario_en";
    }

    @PostMapping("/cotizaciones_en")
    public String procesarCotizacionEn(@ModelAttribute("cotizacionDto") CotizacionDto dto,
                                       RedirectAttributes ra) {
        cotizacionService.crearYCargarCotizacion(dto);

        // Flash attribute para que el modal se muestre en /formulario
        ra.addFlashAttribute("cotizacionEnviada", true);

        // PRG
        return "redirect:/formulario_en";
    }
}