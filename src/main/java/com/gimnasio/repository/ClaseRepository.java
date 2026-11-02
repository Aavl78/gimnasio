package com.gimnasio.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gimnasio.model.Clase;
import com.gimnasio.model.Usuario;

public interface ClaseRepository extends JpaRepository<Clase, Long> { //proporcionar operaciones para trabajar en la tabla
    // Obtener todas las clases de un entrenador
    List<Clase> findByEntrenador(Usuario entrenador); //este metodo busca las clases de un entrenador

    // Todas las clases ordenadas por fecha y hora de inicio
    List<Clase> findAllByOrderByFechaHoraInicioAsc();
   

}
