package com.gimnasio.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gimnasio.model.Clase;
import com.gimnasio.model.Pago;
import com.gimnasio.model.Reserva;
import com.gimnasio.model.Rol;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.ClaseRepository;
import com.gimnasio.repository.PagoRepository;
import com.gimnasio.repository.ReservaRepository;
import com.gimnasio.repository.UsuarioRepository;

@Configuration
public class DatosIniciales {

    @Bean
    public CommandLineRunner cargarUsuarios(UsuarioRepository repo, ClaseRepository claseRepo,
                                            PagoRepository pagoRepo, ReservaRepository reservaRepo,
                                            PasswordEncoder encoder) {
        return args -> {
            // ===== Usuarios (Avance I + 2 clientes nuevos) =====
            crearUsuario(repo, encoder, "Entrenador Principal", "100000001", "admin@gimnasio.com", "admin123", "88880000", Rol.ENTRENADOR);
            crearUsuario(repo, encoder, "Laura Mora", "100000003", "entrenador2@gimnasio.com", "entrenador123", "88882222", Rol.ENTRENADOR);
            crearUsuario(repo, encoder, "Cliente Prueba", "200000002", "cliente@gimnasio.com", "cliente123", "88881111", Rol.CLIENTE);
            crearUsuario(repo, encoder, "Carlos Moroso", "200000003", "moroso@gimnasio.com", "cliente123", "88883333", Rol.CLIENTE);
            crearUsuario(repo, encoder, "Ana Rojas", "200000004", "cliente2@gimnasio.com", "cliente123", "88884444", Rol.CLIENTE);

            // ===== Pagos, clases y reservas: solo se crean la primera vez =====
            if (pagoRepo.count() == 0) {
                Usuario admin = repo.findByCorreo("admin@gimnasio.com").get();
                Usuario laura = repo.findByCorreo("entrenador2@gimnasio.com").get();
                Usuario clienteAlDia = repo.findByCorreo("cliente@gimnasio.com").get();
                Usuario moroso = repo.findByCorreo("moroso@gimnasio.com").get();
                Usuario ana = repo.findByCorreo("cliente2@gimnasio.com").get();
                LocalDate hoy = LocalDate.now();

                // Pagos: dos clientes al día y uno con el pago vencido (moroso)
                crearPago(pagoRepo, clienteAlDia, hoy.minusDays(5), 25000.0, hoy.plusDays(25));
                crearPago(pagoRepo, ana, hoy.minusDays(2), 25000.0, hoy.plusDays(28));
                crearPago(pagoRepo, moroso, hoy.minusDays(40), 25000.0, hoy.minusDays(10));

                // Clases (las fechas se calculan desde hoy)
                Clase yoga = crearClase(claseRepo, "Yoga (finalizada)", admin, hoy.minusDays(1).atTime(7, 0), 10);
                Clase pilates = crearClase(claseRepo, "Pilates (llena)", laura, hoy.plusDays(1).atTime(10, 0), 1);
                Clase spinning = crearClase(claseRepo, "Spinning", admin, hoy.plusDays(1).atTime(18, 0), 10);
                crearClase(claseRepo, "Funcional (choca con Spinning)", laura, hoy.plusDays(1).atTime(18, 30), 10);
                crearClase(claseRepo, "Zumba", admin, hoy.plusDays(2).atTime(17, 0), 10);

                // Reservas
                reservaRepo.save(new Reserva(ana, pilates));            // llena Pilates (cupo 1)
                reservaRepo.save(new Reserva(clienteAlDia, spinning));  // para C5 y C6
                reservaRepo.save(new Reserva(clienteAlDia, yoga));      // clase pasada, para C7
            }
        };
    }

    private void crearUsuario(UsuarioRepository repo, PasswordEncoder encoder, String nombre, String cedula,
                              String correo, String password, String telefono, Rol rol) {
        if (!repo.existsByCorreo(correo)) {
            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setCedula(cedula);
            u.setCorreo(correo);
            u.setPassword(encoder.encode(password));
            u.setTelefono(telefono);
            u.setRol(rol);
            repo.save(u);
        }
    }

    private void crearPago(PagoRepository pagoRepo, Usuario cliente, LocalDate fechaPago,
                           Double monto, LocalDate vencimiento) {
        Pago p = new Pago();
        p.setCliente(cliente);
        p.setFechaPago(fechaPago);
        p.setMonto(monto);
        p.setFechaVencimiento(vencimiento);
        pagoRepo.save(p);
    }

    private Clase crearClase(ClaseRepository claseRepo, String nombre, Usuario entrenador,
                             LocalDateTime inicio, int cupo) {
        Clase c = new Clase();
        c.setNombre(nombre);
        c.setEntrenador(entrenador);
        c.setFechaHoraInicio(inicio);
        c.setCupoMaximo(cupo);
        return claseRepo.save(c);
    }
}
