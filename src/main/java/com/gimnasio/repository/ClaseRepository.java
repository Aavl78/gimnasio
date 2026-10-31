package com.gimnasio.repository;
import com.gimnasio.model.Clase;
import com.gimnasio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ClaseRepository extends JpaRepository<Clase, Long> { //proporcionar operaciones para trabajar en la tabla
    // Obtener todas las clases de un entrenador
    List<Clase> findByEntrenador(Usuario entrenador); //este metodo busca las clases de un entrenador

    // Comprobar si el entrenador tiene otra clase en ese horario
    boolean existsByEntrenadorAndCanceladaFalseAndFechaHoraInicioLessThanAndFechaHoraFinGreaterThan(
            Usuario entrenador,
            LocalDateTime nuevaFin,
            LocalDateTime nuevaInicio
    );
}
