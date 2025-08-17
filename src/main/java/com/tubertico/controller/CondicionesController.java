package com.tubertico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CondicionesController {

    @GetMapping("/condiciones")
    public String mostrarCondiciones() {
        return "condiciones";
    }

    @GetMapping("/condiciones_en")
    public String mostrarCondicionesEn() {
        return "condiciones_en";
    }
}
