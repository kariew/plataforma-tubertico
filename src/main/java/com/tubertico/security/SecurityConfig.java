package com.tubertico.security;

import com.tubertico.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(LoginSuccessHandler loginSuccessHandler,
                          CustomOAuth2UserService customOAuth2UserService) {
        this.loginSuccessHandler = loginSuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(UsuarioService usuarioService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/home", "/home_en", "/login", "/login_en", "/register", "/register_en", "/completar-registro", "/completar-registro_en",
                                "/productos", "/productos_en", "/contacto", "/contacto_en", "/galeria", "/galeria_en",
                                "/cotizaciones_en", "/formulario_en", "/forgot-password", "/forgot-password_en", "/reset-password", "/reset-password_en",
                                "/condiciones", "/condiciones_en", "/politica", "/politica_en",
                                "/css/**", "/img/**", "/js/**", "/static/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/presentaciones", "/api/presentaciones/**").permitAll()
                        .requestMatchers("/formulario").hasAnyRole("CLIENTE", "ADMIN")
                        .requestMatchers("/admin-fichas/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(loginSuccessHandler)
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .successHandler(loginSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}