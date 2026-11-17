package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gimnasio.model.Usuario;
import com.gimnasio.service.ClienteService;
import com.gimnasio.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/clientes")
public class AdminClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;   // NUEVO

    // Spring entrega los dos services
    public AdminClienteController(ClienteService clienteService, UsuarioService usuarioService) {
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;      // NUEVO
    }

    // Lista de todos los clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "admin/clientes";
    }

    //muestra el formulario vacío para crear un cliente
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/cliente-nuevo";
    }

    //recibe el formulario y crea el cliente
    @PostMapping("/nuevo")
    public String crear(@Valid @ModelAttribute("usuario") Usuario usuario,
                        BindingResult resultado,
                        Model model,
                        RedirectAttributes redirect) {
        if (resultado.hasErrors()) {
            return "admin/cliente-nuevo";
        }
        try {
            usuarioService.registrarCliente(usuario);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/cliente-nuevo";
        }
        redirect.addFlashAttribute("exito", "Cliente creado correctamente");
        return "redirect:/admin/clientes";
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