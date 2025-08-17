package com.tubertico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactoController {

    @GetMapping("/contacto")
    public String mostrarContacto(Model model) {
        model.addAttribute("logoNaranja", true); // usa logo naranja
        return "contacto_page";
    }

    @GetMapping("/contacto_en")
    public String mostrarContactoEn(Model model) {
        model.addAttribute("logoNaranja", true);
        return "contacto_page_en";
    }
}
