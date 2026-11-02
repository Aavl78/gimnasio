package com.gimnasio.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.ClienteService;

@Controller
@RequestMapping("/cliente/perfil")
public class PerfilController {

    private final ClienteService clienteService;

    public PerfilController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String verPerfil(Principal principal, Model model) {
        Usuario cliente = clienteService.buscarPorCorreo(principal.getName());
        model.addAttribute("cliente", cliente);
        return "cliente/perfil";
    }

    @PostMapping
    public String actualizar(Principal principal,
                             @RequestParam("nombre") String nombre,
                             @RequestParam("cedula") String cedula,
                             @RequestParam("telefono") String telefono,
                             RedirectAttributes redirect) {

        Usuario cliente = clienteService.buscarPorCorreo(principal.getName());

        try {
            clienteService.actualizarDatos(
                    cliente.getId(), nombre, cedula,
                    cliente.getCorreo(), telefono);

            redirect.addFlashAttribute(
                    "exito", "Perfil actualizado correctamente");

        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/perfil";
    }

    @PostMapping("/password")
    public String cambiarPassword(Principal principal,
                                  @RequestParam("nuevaPassword") String nuevaPassword,
                                  RedirectAttributes redirect) {

        Usuario cliente = clienteService.buscarPorCorreo(principal.getName());

        try {
            clienteService.cambiarPassword(cliente.getId(), nuevaPassword);

            redirect.addFlashAttribute(
                    "exito", "Contraseña cambiada correctamente");

        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/perfil";
    }
}
