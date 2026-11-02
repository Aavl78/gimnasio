package com.gimnasio.service;

import com.gimnasio.model.Clase;
import com.gimnasio.model.Rol;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.ClaseRepository;
import com.gimnasio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//clase para manejar la logica de negocio de las clases
@Service 
public class ClaseService {
 
    private final ClaseRepository claseRepository; //inyectar dependencia del repositorio de clases para acceder a la base de datos
    private final UsuarioRepository usuarioRepository; 
    // Constructor para inyectar las dependencias del repositorio de clases y usuarios    
    public ClaseService(ClaseRepository claseRepository, UsuarioRepository usuarioRepository) {
        this.claseRepository = claseRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Todas las clases (para el administrador)
    public List<Clase> ListarTodas(){
        return claseRepository.findAllByOrderByFechaHoraInicioAsc();
    }

    // Solo las clases no canceladas que todavía no empiezan (para el cliente)
    public List<Clase> listarDisponibles() {
        List<Clase> disponibles = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (Clase c : claseRepository.findAllByOrderByFechaHoraInicioAsc()) {
            if (!c.isCancelada() && c.getFechaHoraInicio().isAfter(ahora)) {
                disponibles.add(c);
            }
        }
        return disponibles;
    }

    public Clase buscarPorId(Long id) {
        return claseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
    }

    public List<Usuario> listarEntrenadores() {
        return usuarioRepository.findByRol(Rol.ENTRENADOR);
    }

    // Crea o edita una clase aplicando las reglas de negocio
    public void guardar(Clase clase, Long entrenadorId) {
        // 1. Buscar y asignar el entrenador
        Usuario entrenador = usuarioRepository.findById(entrenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Entrenador no encontrado"));
        if (entrenador.getRol() != Rol.ENTRENADOR) {
            throw new IllegalArgumentException("El usuario elegido no es entrenador");
        }

        clase.setEntrenador(entrenador);

        // 2. Validar los datos obligatorios y las reglas de negocio
        if (clase.getNombre() == null || clase.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la clase es obligatorio");
        }

        if (clase.getFechaHoraInicio() == null) {
            throw new IllegalArgumentException("La fecha y hora de inicio es obligatoria");
        }

        if (clase.getCupoMaximo() == null || clase.getCupoMaximo() < 1) {
            throw new IllegalArgumentException("El cupo máximo debe ser al menos 1");
        }

        // 3. Validar que el entrenador no tenga otra clase en ese horario
        if (hayTraslape(clase, entrenador)) {
            throw new IllegalArgumentException("El entrenador ya tiene otra clase que se cruza con ese horario");
        }

        //4 guardar (la hora del fin se calcula sola en la clase)
        claseRepository.save(clase);

    }

    // ¿El entrenador tiene otra clase que se cruce con la nueva?
    private boolean hayTraslape(Clase nueva, Usuario entrenador) {
        LocalDateTime inicio = nueva.getFechaHoraInicio();
        LocalDateTime fin = inicio.plusHours(1);

        for (Clase otra : claseRepository.findByEntrenador(entrenador)) {
            if (otra.isCancelada()) {
                continue;                   // las canceladas no cuentan
            }
            if (otra.getId().equals(nueva.getId())) {
                continue;                   // al editar, no se compara consigo misma
            }
            if (otra.getFechaHoraInicio().isBefore(fin) && otra.getFechaHoraFin().isAfter(inicio)) {
                return true;                // se cruzan
            }
        }
        return false;
    }

    //cancelar no borra la clase, solo la marca como cancelada
    public void cancelar(Long id) {
        Clase clase = buscarPorId(id);
        clase.setCancelada(true);
        claseRepository.save(clase);
    }
}
