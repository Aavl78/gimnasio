
package com.gimnasio.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gimnasio.model.Rol;
import com.gimnasio.model.Usuario;
import com.gimnasio.repository.UsuarioRepository;

@Service
public class ClienteService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarClientes() {
        return usuarioRepository.findByRol(Rol.CLIENTE);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public void actualizarDatos(Long id, String nombre, String cedula,
                                String correo, String telefono) {

        if (nombre.isBlank() || cedula.isBlank() || correo.isBlank()) {
            throw new IllegalArgumentException(
                    "Nombre, cédula y correo son obligatorios");
        }

        if (!correo.contains("@")) {
            throw new IllegalArgumentException(
                    "El correo no tiene un formato válido");
        }

        Usuario cliente = buscarPorId(id);

        if (!cliente.getCorreo().equals(correo)
                && usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException(
                    "Ya existe otro usuario con ese correo");
        }

        if (!cliente.getCedula().equals(cedula)
                && usuarioRepository.existsByCedula(cedula)) {
            throw new IllegalArgumentException(
                    "Ya existe otro usuario con esa cédula");
        }

        cliente.setNombre(nombre);
        cliente.setCedula(cedula);
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);

        usuarioRepository.save(cliente);
    }

    public void cambiarPassword(Long id, String nuevaPassword) {

        if (nuevaPassword == null || nuevaPassword.length() < 6) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 6 caracteres");
        }

        Usuario cliente = buscarPorId(id);

        cliente.setPassword(passwordEncoder.encode(nuevaPassword));

        usuarioRepository.save(cliente);
    }

    public void desactivar(Long id) {
        Usuario cliente = buscarPorId(id);
        cliente.setActivo(false);
        usuarioRepository.save(cliente);
    }

    public void activar(Long id) {
        Usuario cliente = buscarPorId(id);
        cliente.setActivo(true);
        usuarioRepository.save(cliente);
    }
}
