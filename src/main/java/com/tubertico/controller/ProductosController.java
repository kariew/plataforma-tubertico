package com.tubertico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductosController {

    @GetMapping("/productos")
    public String mostrarProductos() {
        return "productos"; // nombre del archivo productos.html (sin extensión)
    }

    @GetMapping("/productos_en")
    public String mostrarProductosEn() {
        return "productos_en";
    }
}
