package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gimnasio.service.ClaseService;

@Controller
public class ClienteClaseController {

    private final ClaseService claseService;

    public ClienteClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @GetMapping("/cliente/clases")
    public String verClases(Model model) {
        model.addAttribute("clases", claseService.listarDisponibles());
        return "cliente/clases";
    }
}
