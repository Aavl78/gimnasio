package com.gimnasio.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.ClienteService;
import com.gimnasio.service.PagoService;

@Controller
@RequestMapping("/cliente/pagos")
public class ClientePagoController {

    private final PagoService pagoService;
    private final ClienteService clienteService;

    public ClientePagoController(PagoService pagoService, ClienteService clienteService) {
        this.pagoService = pagoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String verPagos(Principal principal, Model model) {
        // El cliente sale de la sesión: solo puede ver SUS pagos
        Usuario cliente = clienteService.buscarPorCorreo(principal.getName());
        model.addAttribute("alDia", pagoService.estaAlDia(cliente.getId()));
        model.addAttribute("ultimoPago", pagoService.ultimoPago(cliente.getId()));
        model.addAttribute("pagos", pagoService.historial(cliente.getId()));
        return "cliente/pagos";
    }
}