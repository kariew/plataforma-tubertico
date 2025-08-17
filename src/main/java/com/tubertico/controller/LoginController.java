package com.tubertico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";  // nombre del archivo login.html
    }

    @GetMapping("/login_en")
    public String mostrarLoginEn() {
        return "login_en";
    }
}
