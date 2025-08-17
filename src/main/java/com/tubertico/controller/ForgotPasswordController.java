package com.tubertico.controller;

import com.tubertico.model.RecoveryToken;
import com.tubertico.model.Usuario;
import com.tubertico.repository.RecoveryTokenRepository;
import com.tubertico.repository.UsuarioRepository;
import com.tubertico.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String mostrarFormulario() {
        return "forgot-password";
    }

    @GetMapping("/forgot-password_en")
    public String mostrarFormularioEn() {
        return "forgot-password_en";
    }

    @PostMapping("/forgot-password")
    public String procesarFormulario(@RequestParam String correo, Model model) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(correo);

        if (optionalUsuario.isEmpty() || optionalUsuario.get().getCorreo() == null || optionalUsuario.get().getCorreo().isBlank()) {
            model.addAttribute("error", "No existe una cuenta registrada con ese correo.");
            return "forgot-password";
        }

        Usuario usuario = optionalUsuario.get();
        String token = UUID.randomUUID().toString();

        // Guardar token con expiración de 30 minutos
        RecoveryToken recoveryToken = new RecoveryToken(token, LocalDateTime.now().plusMinutes(30), usuario);
        recoveryTokenRepository.save(recoveryToken);

        try {
            emailService.enviarCorreoRecuperacion(usuario.getCorreo(), usuario.getNombre(), token);
            model.addAttribute("mensaje", "Se envió un correo con el enlace para restablecer la contraseña.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            model.addAttribute("error", "Hubo un error al enviar el correo. Intente más tarde.");
        }

        return "forgot-password";
    }
}
