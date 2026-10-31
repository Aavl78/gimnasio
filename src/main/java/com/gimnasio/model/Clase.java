package com.gimnasio.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import  jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity 
@Table(name= "clases")
public class Clase {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id se genera automatico 
    private Long id;
    @NotBlank(message = "El nombre de la clase es obligatorio")
    @Column(nullable = false)
    private String nombre; //ej: crossfit, spinning, yoga

    //Muchas clases pertenecen a un entrenador
    @NotNull (message = "Debe elegir un entrenador")
    @ManyToOne //muchos a uno
    @JoinColumn (name = "entreador id", nullable = false) //crea la llave foranea de la otra columna
    private Usuario entrenador;

    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column (nullable= false)
    private LocalDateTime fechaHoraFin;  // se calcula sola, no tiene setter

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo debe ser al menos 1")
    @Column(nullable = false)
    private Integer cupoMaximo;

    private boolean cancelada = false;
    
    public  Clase(){
    }

    // Se ejecuta automáticamente antes de guardar o actualizar
    public void calcularHoraFin(){
        if (fechaHoraInicio != null) {
            fechaHoraFin = fechaHoraInicio.plusHours(1);
        }

    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Usuario entrenador) {
        this.entrenador = entrenador;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
        calcularHoraFin();
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

}
