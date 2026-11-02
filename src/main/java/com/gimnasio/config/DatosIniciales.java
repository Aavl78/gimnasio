package com.gimnasio.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gimnasio.model.Rol;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.UsuarioRepository;

@Configuration
public class DatosIniciales {
@Bean
    public CommandLineRunner cargarUsuarios(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByCorreo("admin@gimnasio.com")) {
                Usuario admin = new Usuario();
                admin.setNombre("Entrenador Principal");
                admin.setCedula("100000001");
                admin.setCorreo("admin@gimnasio.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setTelefono("88880000");
                admin.setRol(Rol.ENTRENADOR);
                repo.save(admin);
            }
              if (!repo.existsByCorreo("entrenador2@gimnasio.com")) {
                Usuario entrenador2 = new Usuario();
                entrenador2.setNombre("Laura Mora");
                entrenador2.setCedula("100000003");
                entrenador2.setCorreo("entrenador2@gimnasio.com");
                entrenador2.setPassword(encoder.encode("entrenador123"));
                entrenador2.setTelefono("88882222");
                entrenador2.setRol(Rol.ENTRENADOR);
                repo.save(entrenador2);
            }
            if (!repo.existsByCorreo("cliente@gimnasio.com")) {
                Usuario cliente = new Usuario();
                cliente.setNombre("Cliente Prueba");
                cliente.setCedula("200000002");
                cliente.setCorreo("cliente@gimnasio.com");
                cliente.setPassword(encoder.encode("cliente123"));
                cliente.setTelefono("88881111");
                cliente.setRol(Rol.CLIENTE);
                repo.save(cliente);
            }
        };
    }
}

