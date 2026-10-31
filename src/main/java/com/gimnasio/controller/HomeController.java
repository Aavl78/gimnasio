package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
     @GetMapping("/login")
    public String login() {
        return "login";      // muestra templates/login.html
    }

    @GetMapping("/")
    public String inicio() {
        return "inicio";     // muestra templates/inicio.html
    }
}
