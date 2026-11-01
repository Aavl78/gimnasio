package com.gimnasio.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gimnasio.model.Rol;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registra un cliente nuevo
    public void registrarCliente(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con esa cédula");
        }
        validarPassword(usuario.getPassword());

        usuario.setId(null);                 // siempre es un usuario nuevo
        usuario.setRol(Rol.CLIENTE);         // quien se registra siempre es cliente
        usuario.setActivo(true);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    // Revisa que la contraseña tenga al menos 6 caracteres
    private void validarPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
    }
}
