package com.gimnasio.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gimnasio.model.Pago;
import com.gimnasio.model.Rol;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.PagoRepository;
import com.gimnasio.repository.UsuarioRepository;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;

    public PagoService(PagoRepository pagoRepository, UsuarioRepository usuarioRepository) {
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Registra un pago aplicando las validaciones del enunciado
    @Transactional
    public Pago registrar(Long clienteId, Pago pago) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        if (cliente.getRol() != Rol.CLIENTE) {
            throw new IllegalArgumentException("Solo se pueden registrar pagos a clientes");
        }
        if (!cliente.isActivo()) {
            throw new IllegalArgumentException("No se puede registrar el pago: el cliente está inactivo");
        }
        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        if (pago.getFechaPago() == null) {
            throw new IllegalArgumentException("La fecha de pago es obligatoria");
        }
        if (pago.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria");
        }
        if (!pago.getFechaVencimiento().isAfter(pago.getFechaPago())) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a la fecha de pago");
        }

        pago.setCliente(cliente);
        return pagoRepository.save(pago);
    }

    // Historial de pagos del cliente (más reciente primero)
    public List<Pago> historial(Long clienteId) {
        return pagoRepository.findByClienteIdOrderByFechaPagoDesc(clienteId);
    }

    // Último pago del cliente, o null si nunca ha pagado
    public Pago ultimoPago(Long clienteId) {
        return pagoRepository.findFirstByClienteIdOrderByFechaPagoDesc(clienteId).orElse(null);
    }

    // Al día = tiene pagos y el vencimiento de su último pago todavía no pasó
    public boolean estaAlDia(Long clienteId) {
        Pago ultimo = ultimoPago(clienteId);
        if (ultimo == null) {
            return false;                       // sin pagos = moroso
        }
        LocalDate hoy = LocalDate.now();
        return !ultimo.getFechaVencimiento().isBefore(hoy);   // si vence hoy, todavía está al día
    }

    // Clientes activos que NO están al día
    public List<Usuario> listarMorosos() {
        List<Usuario> morosos = new ArrayList<>();
        for (Usuario c : usuarioRepository.findByRol(Rol.CLIENTE)) {
            if (c.isActivo() && !estaAlDia(c.getId())) {
                morosos.add(c);
            }
        }
        return morosos;
    }
}