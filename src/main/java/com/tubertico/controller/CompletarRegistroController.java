package com.tubertico.controller;

import com.tubertico.model.Usuario;
import com.tubertico.repository.UsuarioRepository;
import com.tubertico.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CompletarRegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/completar-registro")
    public String mostrarFormulario(Model model, Authentication authentication) {
        String correo = authentication.getName(); // del login
        Usuario usuario = usuarioService.buscarPorCorreo(correo);

        model.addAttribute("usuario", usuario);
        return "completar-registro";
    }

    @GetMapping("/completar-registro_en")
    public String mostrarFormularioEn(Model model, Authentication authentication) {
        String correo = authentication.getName();
        Usuario usuario = usuarioService.buscarPorCorreo(correo);
        model.addAttribute("usuario", usuario);
        return "completar-registro_en";
    }

    @PostMapping("/completar-registro")
    public String guardarDatos(@ModelAttribute("usuario") Usuario usuarioActualizado) {
        // Guarda los campos editables
        Usuario usuario = usuarioService.buscarPorCorreo(usuarioActualizado.getCorreo());

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmpresa(usuarioActualizado.getEmpresa());
        usuario.setPais(usuarioActualizado.getPais());
        usuario.setTelefono(usuarioActualizado.getTelefono());

        usuarioRepository.save(usuario);

        return "redirect:/formulario";
    }

    @PostMapping("/completar-registro_en")
    public String guardarDatosEn(@ModelAttribute("usuario") Usuario usuarioActualizado) {
        Usuario usuario = usuarioService.buscarPorCorreo(usuarioActualizado.getCorreo());
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmpresa(usuarioActualizado.getEmpresa());
        usuario.setPais(usuarioActualizado.getPais());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuarioRepository.save(usuario);
        return "redirect:/formulario_en";
    }
}
