package com.gimnasio.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gimnasio.model.Clase;
import com.gimnasio.model.EstadoReserva;
import com.gimnasio.model.Reserva;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.ClaseRepository;
import com.gimnasio.repository.ReservaRepository;
import com.gimnasio.repository.UsuarioRepository;

@Service
public class ReservaService {

    // R7: minutos después del inicio de la clase en que el cliente todavía puede cancelar
    private static final int MINUTOS_LIMITE_CANCELACION = 15;

    private final ReservaRepository reservaRepository;
    private final ClaseRepository claseRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoService pagoService;

    public ReservaService(ReservaRepository reservaRepository, ClaseRepository claseRepository,
                          UsuarioRepository usuarioRepository, PagoService pagoService) {
        this.reservaRepository = reservaRepository;
        this.claseRepository = claseRepository;
        this.usuarioRepository = usuarioRepository;
        this.pagoService = pagoService;
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
    }

    // MATRÍCULA: revisa las reglas una por una. Si una falla, lanza su mensaje y NO se guarda nada
    @Transactional
    public Reserva matricular(Long clienteId, Long claseId) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
        LocalDateTime ahora = LocalDateTime.now();

        // R1: el cliente debe estar al día con el pago
        if (!pagoService.estaAlDia(clienteId)) {
            throw new IllegalArgumentException(
                    "No puede matricularse: su membresía está vencida. Póngase al día con el pago para reservar clases");
        }

        // R3: la clase no puede estar cancelada ni finalizada
        if (clase.isCancelada()) {
            throw new IllegalArgumentException("No puede matricularse: la clase fue cancelada");
        }
        if (!clase.getFechaHoraFin().isAfter(ahora)) {
            throw new IllegalArgumentException("No puede matricularse: la clase ya finalizó");
        }

        // R5: no puede tener dos reservas activas en la misma clase
        if (reservaRepository.existsByClienteIdAndClaseIdAndEstado(clienteId, claseId, EstadoReserva.ACTIVA)) {
            throw new IllegalArgumentException("Ya tiene una reserva activa en esta clase");
        }

        // R2: las reservas activas deben ser menos que el cupo máximo
        long ocupados = reservaRepository.countByClaseIdAndEstado(claseId, EstadoReserva.ACTIVA);
        if (ocupados >= clase.getCupoMaximo()) {
            throw new IllegalArgumentException("No hay cupos disponibles: la clase está llena");
        }

        // R4: no puede tener otra reserva activa que se cruce con este horario
        List<Reserva> choques = reservaRepository.buscarTraslapes(clienteId, EstadoReserva.ACTIVA,
                clase.getFechaHoraInicio(), clase.getFechaHoraFin());
        if (!choques.isEmpty()) {
            throw new IllegalArgumentException("Ya tiene una reserva en '"
                    + choques.get(0).getClase().getNombre() + "' que se cruza con este horario");
        }

        // Si pasó todas las reglas, se guarda la reserva como ACTIVA
        Reserva reserva = new Reserva(cliente, clase);
        return reservaRepository.save(reserva);
    }

    // CANCELACIÓN POR EL CLIENTE (R6 y R7)
    @Transactional
    public void cancelarPorCliente(Long reservaId, Long clienteId) {
        Reserva reserva = buscarPorId(reservaId);

        // El cliente solo puede cancelar SUS reservas
        if (!reserva.getCliente().getId().equals(clienteId)) {
            throw new IllegalArgumentException("No puede cancelar una reserva que no es suya");
        }
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalArgumentException("La reserva ya estaba cancelada");
        }

        // R7: se puede cancelar hasta 15 minutos después del inicio de la clase
        LocalDateTime limite = reserva.getClase().getFechaHoraInicio().plusMinutes(MINUTOS_LIMITE_CANCELACION);
        if (LocalDateTime.now().isAfter(limite)) {
            throw new IllegalArgumentException("Ya no puede cancelar: el límite es de "
                    + MINUTOS_LIMITE_CANCELACION + " minutos después del inicio de la clase");
        }

        // R6: no se borra, solo cambia a CANCELADA (y el cupo se libera solo)
        reserva.cancelar("CLIENTE");
        reservaRepository.save(reserva);
    }

    // Reservas activas: estado ACTIVA y la clase todavía no termina
    public List<Reserva> listarActivasDelCliente(Long clienteId) {
        List<Reserva> activas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        for (Reserva r : reservaRepository.findByClienteIdOrderByClaseFechaHoraInicioDesc(clienteId)) {
            if (r.getEstado() == EstadoReserva.ACTIVA && r.getClase().getFechaHoraFin().isAfter(ahora)) {
                activas.add(r);
            }
        }
        return activas;
    }

    // Historial: reservas canceladas o de clases que ya pasaron
    public List<Reserva> listarHistorialDelCliente(Long clienteId) {
        List<Reserva> historial = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        for (Reserva r : reservaRepository.findByClienteIdOrderByClaseFechaHoraInicioDesc(clienteId)) {
            if (r.getEstado() == EstadoReserva.CANCELADA || !r.getClase().getFechaHoraFin().isAfter(ahora)) {
                historial.add(r);
            }
        }
        return historial;
    }

    // ===== Monitoreo del administrador (filtros) =====

    // Filtra por clase, por estado, por ambos o sin filtro
    public List<Reserva> filtrar(Long claseId, EstadoReserva estado) {
        if (claseId != null && estado != null) {
            return reservaRepository.findByClaseIdAndEstadoOrderByFechaReservaDesc(claseId, estado);
        }
        if (claseId != null) {
            return reservaRepository.findByClaseIdOrderByFechaReservaDesc(claseId);
        }
        if (estado != null) {
            return reservaRepository.findByEstadoOrderByFechaReservaDesc(estado);
        }
        return reservaRepository.findAllByOrderByFechaReservaDesc();
    }

    // R8: el administrador puede cancelar cualquier reserva (sin límite de 15 minutos)
    @Transactional
    public void cancelarPorAdmin(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId);

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalArgumentException("La reserva ya estaba cancelada");
        }

        reserva.cancelar("ADMINISTRADOR");
        reservaRepository.save(reserva);
    }

}
