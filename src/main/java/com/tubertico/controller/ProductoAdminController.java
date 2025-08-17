package com.tubertico.controller;

import com.tubertico.model.Producto;
import com.tubertico.model.Presentacion;
import com.tubertico.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin-fichas")
public class ProductoAdminController {

    private final ProductoService productoService;

    public ProductoAdminController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("productosExistentes", productoService.listarTodos());
        return "admin-fichas";
    }

    @PostMapping
    public String guardarProducto(@ModelAttribute Producto producto) {
        // 🔧 Enlace correcto entre producto y sus presentaciones
        if (producto.getPresentaciones() != null) {
            for (Presentacion p : producto.getPresentaciones()) {
                p.setProducto(producto);
            }
        }

        productoService.guardar(producto);
        return "redirect:/admin-fichas";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerPorId(id).orElse(new Producto());
        model.addAttribute("producto", producto);
        model.addAttribute("productosExistentes", productoService.listarTodos());
        return "admin-fichas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/admin-fichas";
    }
}
