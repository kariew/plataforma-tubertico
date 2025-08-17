package com.tubertico.controller;

import com.tubertico.model.Usuario;
import com.tubertico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register"; // nombre del archivo register.html
    }

    @GetMapping("/register_en")
    public String mostrarFormularioRegistroEn(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register_en";
    }

    @PostMapping("/register")
    public String procesarFormularioRegistro(@ModelAttribute("usuario") Usuario usuario,
                                             @RequestParam("confirmarPassword") String confirmarPassword,
                                             Model model) {
        if (!usuario.getPassword().equals(confirmarPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "register";
        }

        if (usuarioService.existePorEmail(usuario.getCorreo())) {
            model.addAttribute("error", "Ya existe una cuenta con este correo.");
            return "register";
        }

        usuarioService.guardarUsuario(usuario);
        return "redirect:/login?registro_exitoso";
    }
}
