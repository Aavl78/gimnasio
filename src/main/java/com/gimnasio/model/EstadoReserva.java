package com.gimnasio.model;

public enum EstadoReserva {

    ACTIVA("Activa"),
    CANCELADA("Cancelada");

    private final String etiqueta;  // texto bonito para mostrar en las vistas

    EstadoReserva(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
