package com.tubertico.security;

import com.tubertico.model.Usuario;
import com.tubertico.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;

    public LoginSuccessHandler(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String correo;

        if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
            correo = (String) oauthUser.getAttribute("email");
            System.out.println("🔐 Login con Google, correo: " + correo);
        } else {
            correo = authentication.getName();
            System.out.println("🔐 Login tradicional, correo: " + correo);
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            // Esto es raro, ya debería estar creado en CustomOAuth2UserService
            System.out.println("⚠️ Usuario no encontrado tras login, redirigiendo a completar-registro");
            response.sendRedirect("/completar-registro");
            return;
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.isCompletarDatos()) {
            System.out.println("🔄 Usuario debe completar datos");
            response.sendRedirect("/completar-registro");
        } else {
            System.out.println("✅ Usuario completo, redirigiendo a /formulario");
            response.sendRedirect("/formulario");
        }
    }
}
