package com.tubertico.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class WebController implements WebMvcConfigurer {

    @GetMapping({"/", "/home"})
    public String mostrarHome(Model model) {
        model.addAttribute("logoNaranja", false); // usa logo verde
        return "home";
    }

    @GetMapping("/home_en")
    public String mostrarHomeEn(Model model) {
        model.addAttribute("logoNaranja", false);
        return "home_en";
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IdiomaInterceptor());
    }

    public static class IdiomaInterceptor implements HandlerInterceptor {
        private static final String[] EXCLUDE_PATHS = {
            "/login", "/login_en", "/register", "/register_en", "/logout", "/error", "/forgot-password", "/forgot-password_en", "/reset-password", "/reset-password_en", "/oauth2", "/api/", "/css/", "/js/", "/img/", "/static/"
        };
        private boolean isExcluded(String uri) {
            for (String ex : EXCLUDE_PATHS) {
                if (uri.equals(ex) || uri.startsWith(ex)) return true;
            }
            return false;
        }
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String uri = request.getRequestURI();
            if (isExcluded(uri) || uri.contains(".")) {
                return true; // No redirigir recursos estáticos ni rutas excluidas
            }
            String lang = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("lang".equals(cookie.getName())) {
                        lang = cookie.getValue();
                        break;
                    }
                }
            }
            // Si no hay cookie, detectar idioma del navegador y establecer cookie
            if (lang == null) {
                String acceptLang = request.getHeader("Accept-Language");
                if (acceptLang != null && acceptLang.toLowerCase().startsWith("en")) {
                    // Set cookie and redirect to English version
                    Cookie cookie = new Cookie("lang", "en");
                    cookie.setPath("/");
                    cookie.setMaxAge(31536000); // 1 año
                    response.addCookie(cookie);
                    boolean isEn = uri.endsWith("_en") || uri.endsWith("_en/");
                    if (!isEn) {
                        if ((uri.equals("/") || uri.equals("/home"))) {
                            response.sendRedirect("/home_en");
                        } else {
                            response.sendRedirect(uri.endsWith("/") ? uri.substring(0, uri.length() - 1) + "_en" : uri + "_en");
                        }
                        return false;
                    }
                } else if (acceptLang != null && acceptLang.toLowerCase().startsWith("es")) {
                    // Set cookie to es (opcional, español es default)
                    Cookie cookie = new Cookie("lang", "es");
                    cookie.setPath("/");
                    cookie.setMaxAge(31536000);
                    response.addCookie(cookie);
                    // No redirigir, español es default
                }
                return true;
            }
            boolean isEn = uri.endsWith("_en") || uri.endsWith("_en/");
            boolean isEs = !isEn;
            // Evitar bucles: si ya estamos en la versión correcta, no redirigir
            if (lang.equals("en")) {
                if ((uri.equals("/") || uri.equals("/home")) && !uri.endsWith("_en")) {
                    response.sendRedirect("/home_en");
                    return false;
                }
                if (!isEn && !uri.equals("/")) {
                    // Solo redirigir si no termina en _en y no es la raíz
                    // Evitar doble _en
                    if (!uri.endsWith("_en")) {
                        response.sendRedirect(uri.endsWith("/") ? uri.substring(0, uri.length() - 1) + "_en" : uri + "_en");
                        return false;
                    }
                }
            }
            if (lang.equals("es") && isEn) {
                // Redirigir a versión en español solo si termina en _en
                String uriEs = uri.replaceAll("_en/?$", "");
                if (uriEs.isEmpty()) uriEs = "/home";
                response.sendRedirect(uriEs);
                return false;
            }
            return true;
        }
    }
}
                                 