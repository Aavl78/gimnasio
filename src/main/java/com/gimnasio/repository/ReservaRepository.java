package com.gimnasio.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gimnasio.model.EstadoReserva;
import com.gimnasio.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // R2 y cupos: cuántas reservas activas tiene una clase
    //SELECT COUNT(*) FROM reservas WHERE clase_id = ? AND estado = ?
    long countByClaseIdAndEstado(Long claseId, EstadoReserva estado);

    // R5: ¿el cliente ya tiene una reserva activa en esa clase?
    boolean existsByClienteIdAndClaseIdAndEstado(Long clienteId, Long claseId, EstadoReserva estado);

    // R9: reservas activas de una clase (para cancelarlas todas)
    List<Reserva> findByClaseIdAndEstado(Long claseId, EstadoReserva estado);

    // Reservas del cliente con un estado (se usa en la modificación "máximo 3 por semana")
    @EntityGraph(attributePaths = "clase")
    List<Reserva> findByClienteIdAndEstado(Long clienteId, EstadoReserva estado);

    // Todas las reservas del cliente, ordenadas por fecha de la clase
    @EntityGraph(attributePaths = {"clase", "clase.entrenador"})
    List<Reserva> findByClienteIdOrderByClaseFechaHoraInicioDesc(Long clienteId);

    // R4: reservas activas del cliente que se cruzan con el horario [inicio, fin)
    @Query("SELECT r FROM Reserva r WHERE r.cliente.id = :clienteId AND r.estado = :estado "
            + "AND r.clase.fechaHoraInicio < :fin AND r.clase.fechaHoraFin > :inicio")
    List<Reserva> buscarTraslapes(@Param("clienteId") Long clienteId,
                                  @Param("estado") EstadoReserva estado,
                                  @Param("inicio") LocalDateTime inicio,
                                  @Param("fin") LocalDateTime fin);
}

