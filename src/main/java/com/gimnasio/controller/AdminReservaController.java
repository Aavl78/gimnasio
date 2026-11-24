package com.gimnasio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gimnasio.model.EstadoReserva;
import com.gimnasio.service.ClaseService;
import com.gimnasio.service.ReservaService;

@Controller
@RequestMapping("/admin/reservas")
public class AdminReservaController {

    private final ReservaService reservaService;
    private final ClaseService claseService;

    public AdminReservaController(ReservaService reservaService, ClaseService claseService) {
        this.reservaService = reservaService;
        this.claseService = claseService;
    }

    // Lista con filtros opcionales: /admin/reservas?claseId=3&estado=ACTIVA
    @GetMapping
    public String listar(@RequestParam(name = "claseId", required = false) Long claseId,
                         @RequestParam(name = "estado", required = false) EstadoReserva estado,
                         Model model) {
        model.addAttribute("reservas", reservaService.filtrar(claseId, estado));
        model.addAttribute("clases", claseService.ListarTodas());
        model.addAttribute("estados", EstadoReserva.values());
        model.addAttribute("claseId", claseId);   // para dejar marcado el filtro elegido
        model.addAttribute("estado", estado);
        return "admin/reservas";
    }

    // R8: cancelar cualquier reserva
    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            reservaService.cancelarPorAdmin(id);
            redirect.addFlashAttribute("exito", "Reserva cancelada por el administrador");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reservas";
    }
}
