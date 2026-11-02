package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gimnasio.model.Clase;
import com.gimnasio.service.ClaseService;

@Controller 
@RequestMapping("/admin/clases") //ruta base para todas las acciones de este controlador
public class AdminClaseController {

        private final ClaseService claseService;

    public AdminClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clases", claseService.ListarTodas());
        return "admin/clases";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("clase", new Clase());
        model.addAttribute("entrenadores", claseService.listarEntrenadores());
        return "admin/clase-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        model.addAttribute("clase", claseService.buscarPorId(id));
        model.addAttribute("entrenadores", claseService.listarEntrenadores());
        return "admin/clase-form";
    }

    // Sirve para crear y para editar
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("clase") Clase clase,
                          @RequestParam("entrenadorId") Long entrenadorId,
                          Model model,
                          RedirectAttributes redirect) {
        try {
            claseService.guardar(clase, entrenadorId);
            redirect.addFlashAttribute("exito", "Clase guardada correctamente");
            return "redirect:/admin/clases";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("entrenadores", claseService.listarEntrenadores());
            return "admin/clase-form";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable("id") Long id, RedirectAttributes redirect) {
        claseService.cancelar(id);
        redirect.addFlashAttribute("exito", "Clase cancelada");
        return "redirect:/admin/clases";
    }
}

    

