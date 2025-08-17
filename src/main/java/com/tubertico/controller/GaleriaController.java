package com.tubertico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GaleriaController {

    @GetMapping("/galeria")
    public String mostrarGaleria(Model model) {
        // Ruta absoluta a la carpeta de imágenes
        String imgDir = "src/main/resources/static/img/";
        File folder = new File(imgDir);
        File[] files = folder.listFiles();
        List<String> productos = new ArrayList<>();
        List<String> compania = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (name.matches("productos\\d+\\.jpg")) {
                    productos.add("/img/" + name);
                } else if (name.matches("compania\\d+\\.jpg")) {
                    compania.add("/img/" + name);
                }
            }
        }
        // Ordenar por nombre para mantener el orden visual
        productos = productos.stream().sorted().collect(Collectors.toList());
        compania = compania.stream().sorted().collect(Collectors.toList());
        model.addAttribute("productos", productos);
        model.addAttribute("compania", compania);
        return "galeria";
    }

    @GetMapping("/galeria_en")
    public String mostrarGaleriaEn(Model model) {
        // Copiar la lógica de mostrarGaleria
        String imgDir = "src/main/resources/static/img/";
        File folder = new File(imgDir);
        File[] files = folder.listFiles();
        List<String> productos = new ArrayList<>();
        List<String> compania = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (name.matches("productos\\d+\\.jpg")) {
                    productos.add("/img/" + name);
                } else if (name.matches("compania\\d+\\.jpg")) {
                    compania.add("/img/" + name);
                }
            }
        }
        productos = productos.stream().sorted().collect(Collectors.toList());
        compania = compania.stream().sorted().collect(Collectors.toList());
        model.addAttribute("productos", productos);
        model.addAttribute("compania", compania);
        return "galeria_en";
    }
}
