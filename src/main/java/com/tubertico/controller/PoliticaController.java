package com.tubertico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PoliticaController {

    @GetMapping("/politica")
    public String mostrarPolitica() {
        return "politica";
    }

    @GetMapping("/politica_en")
    public String mostrarPoliticaEn() {
        return "politica_en";
    }
}
