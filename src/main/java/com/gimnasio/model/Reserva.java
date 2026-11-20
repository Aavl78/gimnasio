package com.gimnasio.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchas reservas pertenecen a un cliente
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    // Muchas reservas pertenecen a una clase
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clase_id", nullable = false)
    private Clase clase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado = EstadoReserva.ACTIVA;

    @Column(nullable = false)
    private LocalDateTime fechaReserva;

    private LocalDateTime fechaCancelacion;

    @Column(length = 30)
    private String canceladaPor;   // CLIENTE, ADMINISTRADOR o CLASE CANCELADA

    public Reserva() {
    }

    public Reserva(Usuario cliente, Clase clase) {
        this.cliente = cliente;
        this.clase = clase;
        this.estado = EstadoReserva.ACTIVA;
        this.fechaReserva = LocalDateTime.now();
    }

    // Cancelar NO borra la reserva: solo cambia el estado (regla R6)
    public void cancelar(String quien) {
        this.estado = EstadoReserva.CANCELADA;
        this.fechaCancelacion = LocalDateTime.now();
        this.canceladaPor = quien;
    }

    // ===== Getters y setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public String getCanceladaPor() {
        return canceladaPor;
    }

    public void setCanceladaPor(String canceladaPor) {
        this.canceladaPor = canceladaPor;
    }
}
