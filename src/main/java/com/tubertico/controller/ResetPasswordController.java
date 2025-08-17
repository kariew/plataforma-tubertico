package com.tubertico.controller;

import com.tubertico.model.Usuario;
import com.tubertico.model.RecoveryToken;
import com.tubertico.repository.UsuarioRepository;
import com.tubertico.repository.RecoveryTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ResetPasswordController {

    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<RecoveryToken> tokenOptional = recoveryTokenRepository.findByToken(token);

        if (tokenOptional.isPresent() && tokenOptional.get().getExpiracion().isAfter(LocalDateTime.now())) {
            model.addAttribute("token", token);
            model.addAttribute("tokenInvalido", false);
        } else {
            model.addAttribute("tokenInvalido", true);
        }

        return "reset-password";
    }

    @GetMapping("/reset-password_en")
    public String showResetPasswordFormEn(@RequestParam("token") String token, Model model) {
        Optional<RecoveryToken> tokenOptional = recoveryTokenRepository.findByToken(token);
        if (tokenOptional.isPresent() && tokenOptional.get().getExpiracion().isAfter(LocalDateTime.now())) {
            model.addAttribute("token", token);
            model.addAttribute("tokenInvalido", false);
        } else {
            model.addAttribute("tokenInvalido", true);
        }
        return "reset-password_en";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirmar") String confirmar,
                                       Model model) {
        Optional<RecoveryToken> tokenOptional = recoveryTokenRepository.findByToken(token);

        if (tokenOptional.isPresent()) {
            RecoveryToken recoveryToken = tokenOptional.get();

            if (recoveryToken.getExpiracion().isBefore(LocalDateTime.now())) {
                model.addAttribute("tokenInvalido", true);
                return "reset-password";
            }

            if (!password.equals(confirmar)) {
                model.addAttribute("token", token);
                model.addAttribute("tokenInvalido", false);
                model.addAttribute("mensaje", "Las contraseñas no coinciden.");
                return "reset-password";
            }

            Usuario usuario = recoveryToken.getUsuario();
            usuario.setPassword(passwordEncoder.encode(password));
            usuarioRepository.save(usuario);
            recoveryTokenRepository.delete(recoveryToken);

            model.addAttribute("mensaje", "Contraseña restablecida correctamente. Ya puedes iniciar sesión.");
            return "login";
        } else {
            model.addAttribute("tokenInvalido", true);
            return "reset-password";
        }
    }
}
