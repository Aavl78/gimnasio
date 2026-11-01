package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gimnasio.service.UsuarioService;

@Controller
public class RecuperarController {

    private final UsuarioService usuarioService;

    public RecuperarController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/recuperar")
    public String mostrarFormulario() {
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String recuperar(@RequestParam("correo") String correo,
                            @RequestParam("cedula") String cedula,
                            @RequestParam("nuevaPassword") String nuevaPassword,
                            Model model) {
        try {
            usuarioService.recuperarPassword(correo, cedula, nuevaPassword);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "recuperar";
        }
        return "redirect:/login?recuperado";
    }
}
