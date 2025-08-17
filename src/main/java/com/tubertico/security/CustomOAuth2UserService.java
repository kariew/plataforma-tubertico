package com.tubertico.security;

import com.tubertico.model.Usuario;
import com.tubertico.repository.UsuarioRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsuarioRepository usuarioRepository;

    public CustomOAuth2UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("🔍 Atributos recibidos de Google: " + attributes);

        String correo = (String) attributes.get("email");
        String nombre = (String) attributes.get("name");

        if (correo == null) {
            throw new RuntimeException("❌ No se pudo obtener el correo electrónico desde Google.");
        }

        Optional<Usuario> existente = usuarioRepository.findByCorreo(correo);
        String rolTmp = "ROLE_CLIENTE"; // valor por defecto
        if (existente.isPresent() && existente.get().getRol() != null) {
            rolTmp = existente.get().getRol();
        }
        final String rol = rolTmp.startsWith("ROLE_") ? rolTmp : "ROLE_" + rolTmp;
        System.out.println("[OAuth2] Rol asignado en memoria: " + rol);

        if (existente.isEmpty()) {
            Usuario nuevo = new Usuario();
            nuevo.setCorreo(correo);
            nuevo.setNombre(nombre != null ? nombre : "");
            nuevo.setEmpresa("");     // campo obligatorio en base de datos
            nuevo.setPais("");
            nuevo.setTelefono("");
            nuevo.setPassword("");    // no se usa para login con Google
            nuevo.setRol("CLIENTE");  // Guarda el rol sin prefijo ROLE_
            usuarioRepository.save(nuevo);
            System.out.println("✅ Usuario nuevo guardado: " + correo);
        } else {
            System.out.println("🟢 Usuario ya existente: " + correo);
        }

        return new DefaultOAuth2User(
                Collections.singleton(() -> rol),
                attributes,
                "email"
        );
    }
}
