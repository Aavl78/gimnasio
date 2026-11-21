package com.gimnasio.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gimnasio.model.Pago;
import com.gimnasio.service.ClienteService;
import com.gimnasio.service.PagoService;

@Controller
@RequestMapping("/admin/pagos")
public class AdminPagoController {

    private final PagoService pagoService;
    private final ClienteService clienteService;

    public AdminPagoController(PagoService pagoService, ClienteService clienteService) {
        this.pagoService = pagoService;
        this.clienteService = clienteService;
    }

    // Estado e historial de pagos de un cliente
    @GetMapping("/cliente/{clienteId}")
    public String historial(@PathVariable("clienteId") Long clienteId, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(clienteId));
        model.addAttribute("pagos", pagoService.historial(clienteId));
        model.addAttribute("alDia", pagoService.estaAlDia(clienteId));
        return "admin/pagos-cliente";
    }

    // Formulario para registrar un pago
    @GetMapping("/cliente/{clienteId}/nuevo")
    public String nuevo(@PathVariable("clienteId") Long clienteId, Model model) {
        Pago pago = new Pago();
        pago.setFechaPago(LocalDate.now());                    // sugerencia: hoy
        pago.setFechaVencimiento(LocalDate.now().plusMonths(1)); // sugerencia: un mes
        model.addAttribute("pago", pago);
        model.addAttribute("cliente", clienteService.buscarPorId(clienteId));
        return "admin/pago-form";
    }

    // Guardar el pago
    @PostMapping("/cliente/{clienteId}/guardar")
    public String guardar(@PathVariable("clienteId") Long clienteId,
                          @ModelAttribute("pago") Pago pago,
                          Model model,
                          RedirectAttributes redirect) {
        try {
            pagoService.registrar(clienteId, pago);
            redirect.addFlashAttribute("exito", "Pago registrado correctamente");
            return "redirect:/admin/pagos/cliente/" + clienteId;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cliente", clienteService.buscarPorId(clienteId));
            return "admin/pago-form";
        }
    }

    // Consulta de clientes morosos
    @GetMapping("/morosos")
    public String morosos(Model model) {
        model.addAttribute("morosos", pagoService.listarMorosos());
        return "admin/morosos";
    }
}