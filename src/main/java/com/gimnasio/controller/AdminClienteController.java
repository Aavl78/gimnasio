package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gimnasio.service.ClienteService;

@Controller
@RequestMapping("/admin/clientes")
public class AdminClienteController {

    private final ClienteService clienteService;

    public AdminClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Lista de todos los clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "admin/clientes";
    }

    // Formulario de edición
    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable("id") Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        return "admin/cliente-editar";
    }

    // Guardar la edición
    @PostMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id,
                         @RequestParam("nombre") String nombre,
                         @RequestParam("cedula") String cedula,
                         @RequestParam("correo") String correo,
                         @RequestParam("telefono") String telefono,
                         RedirectAttributes redirect) {
        try {
            clienteService.actualizarDatos(id, nombre, cedula, correo, telefono);
            redirect.addFlashAttribute("exito", "Cliente actualizado correctamente");
            return "redirect:/admin/clientes";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/clientes/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable("id") Long id, RedirectAttributes redirect) {
        clienteService.desactivar(id);
        redirect.addFlashAttribute("exito", "Cliente desactivado");
        return "redirect:/admin/clientes";
    }

    @PostMapping("/{id}/activar")
    public String activar(@PathVariable("id") Long id, RedirectAttributes redirect) {
        clienteService.activar(id);
        redirect.addFlashAttribute("exito", "Cliente activado");
        return "redirect:/admin/clientes";
    }
}
