package com.gimnasio.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.ClaseService;
import com.gimnasio.service.ClienteService;
import com.gimnasio.service.ReservaService;

@Controller
public class ClienteClaseController {

    private final ClaseService claseService;
    private final ReservaService reservaService;
    private final ClienteService clienteService;

    public ClienteClaseController(ClaseService claseService, ReservaService reservaService,
                                  ClienteService clienteService) {
        this.claseService = claseService;
        this.reservaService = reservaService;
        this.clienteService = clienteService;
    }

    @GetMapping("/cliente/clases")
    public String verClases(Model model) {
        model.addAttribute("clases", claseService.listarParaCliente());
        return "cliente/clases";
    }

    @PostMapping("/cliente/clases/{id}/matricular")
    public String matricular(@PathVariable("id") Long id, Principal principal,
                             RedirectAttributes redirect) {
        Usuario cliente = clienteService.buscarPorCorreo(principal.getName());
        try {
            reservaService.matricular(cliente.getId(), id);
            redirect.addFlashAttribute("exito", "Matrícula realizada correctamente");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cliente/clases";
    }
}
