package com.growza_prueba.growzap.service;

import com.growza_prueba.growzap.model.Carrito;
import com.growza_prueba.growzap.model.Usuarios;
import com.growza_prueba.growzap.repository.IUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService implements IUsuariosService {

    private final IUsuariosRepository usuariosRepository;

    private PasswordEncoder passwordEncoder;

    /*public Usuarios registerUsuario(Usuarios user) {
        if (user.getNombre() == null || user.getApellido() == null ||
                user.getCorreo() == null || user.getContrasena() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        // Verificar si el usuario ya existe
        if (IUsuariosRepository.findByUsername(user.getNombre()) != null) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        Usuarios newUser = new Usuarios();
        newUser.setNombre(user.getNombre());
        newUser.setContrasena(passwordEncoder.encode(user.getContrasena()));
        newUser.setNombre(user.getNombre());
        newUser.setApellido(user.getApellido());

        return usuariosRepository.save(newUser);
    }*/


    // Métdo de carga de usuario implementado desde UserDetailsService
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuarios user = usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        return new org.springframework.security.core.userdetails.User(user.getCorreo(), user.getContrasena(), new ArrayList<>());
    }




    public UsuariosService(IUsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public List<Usuarios> traerTodosLosUsuarios() {
        return usuariosRepository.findAll();
    }

    @Override
    public Optional<Usuarios> traerUsuarioPorEmail(String correo) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuarios> traerUsuarioPorCorreo(String correo) {
        return usuariosRepository.findByCorreo(correo);
    }

    @Override
    public List<Usuarios> traerUsuarioPorNombre(String nombre) {
        return usuariosRepository.findByNombre(nombre);
    }

    // UsuariosService.java

    @Override
    public void crearUsuario(Usuarios usuario) {
        System.out.println("Iniciando el proceso de creación de usuario en el servicio.");
        System.out.println("Encriptando la contraseña...");

        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        if (usuariosRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }
        System.out.println("Guardando el usuario en la base de datos...");
        Carrito carrito = new Carrito();
        usuario.setCarrito(carrito);
        usuariosRepository.save(usuario);
        System.out.println("Usuario guardado exitosamente en la base de datos.");
    }

    @Override
    public void editarUsuario(Long id, Usuarios usuarioActualizado) {
        Optional<Usuarios> usuarioExistente = usuariosRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuarios usuario = usuarioExistente.get();
            usuario.setApellido(usuarioActualizado.getApellido());
            usuario.setCorreo(usuarioActualizado.getCorreo());
            usuario.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
            usuario.setFecha_registro(usuarioActualizado.getFecha_registro());
            usuariosRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
    }

    @Override
    public void eliminarUsuarios(Long id) {
        if (usuariosRepository.existsById(id)) {
            usuariosRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se encontró el usuario con ID: " + id);
        }
    }
}