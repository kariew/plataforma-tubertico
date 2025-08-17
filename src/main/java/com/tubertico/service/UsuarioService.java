package com.tubertico.service;

import com.tubertico.model.Usuario;
import com.tubertico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void guardarUsuario(Usuario usuario) {
        if (usuario.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        // Asignar CLIENTE como rol por defecto si no se define
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("CLIENTE");
        }

        usuarioRepository.save(usuario);
    }

    public boolean existePorEmail(String correo) {
        return usuarioRepository.findByCorreo(correo).isPresent();
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    public boolean validarCredenciales(String correo, String rawPassword) {
        Usuario usuario = buscarPorCorreo(correo);
        return usuario != null && passwordEncoder.matches(rawPassword, usuario.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        System.out.println("Buscando usuario con correo: " + correo);
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo));

        System.out.println("Usuario encontrado en BDD: " + usuario.getCorreo());
        System.out.println("Hash recuperado: " + usuario.getPassword());

        String rol = usuario.getRol();
        if (rol != null && !rol.startsWith("ROLE_")) {
            rol = "ROLE_" + rol;
        }
        final String finalRol = rol;

        return new User(
                usuario.getCorreo(),
                usuario.getPassword() != null ? usuario.getPassword() : "",
                Collections.singletonList(() -> finalRol)
        );
    }
}
