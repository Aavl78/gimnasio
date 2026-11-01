package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Muestra el formulario vacío
    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // Recibe el formulario lleno
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") Usuario usuario,
                            BindingResult resultado,
                            Model model) {
        if (resultado.hasErrors()) {
            return "registro";               // vuelve al formulario mostrando los errores
        }
        try {
            usuarioService.registrarCliente(usuario);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
        return "redirect:/login?registrado";
    }
}
