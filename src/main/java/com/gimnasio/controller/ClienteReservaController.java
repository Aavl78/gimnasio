package com.gimnasio.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.ClienteService;
import com.gimnasio.service.ReservaService;

@Controller
@RequestMapping("/cliente/reservas")
public class ClienteReservaController {

    private final ReservaService reservaService;
    private final ClienteService clienteService;

    public ClienteReservaController(ReservaService reservaService, ClienteService clienteService) {
        this.reservaService = reservaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String misReservas(Principal principal, Model model) {
        Usuario cliente = clienteService.buscarPorCorreo(principal.getName());
        model.addAttribute("activas", reservaService.listarActivasDelCliente(cliente.getId()));
        model.addAttribute("historial", reservaService.listarHistorialDelCliente(cliente.getId()));
        return "cliente/reservas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable("id") Long id, Principal principal,
                           RedirectAttributes redirect) {
        Usuario cliente = clienteService.buscarPorCorreo(principal.getName());
        try {
            reservaService.cancelarPorCliente(id, cliente.getId());
            redirect.addFlashAttribute("exito", "Reserva cancelada. Se liberó un cupo en la clase");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cliente/reservas";
    }
}

