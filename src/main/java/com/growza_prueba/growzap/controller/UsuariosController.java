package com.growza_prueba.growzap.controller;

import com.growza_prueba.growzap.JwtUtil;
import com.growza_prueba.growzap.dto.UserDto;
import com.growza_prueba.growzap.model.Usuarios;
import com.growza_prueba.growzap.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/growza/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuariosController(
            UsuariosService usuariosService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ) {
        this.usuariosService = usuariosService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Usuarios> listaUsuarios() {
        return usuariosService.traerTodosLosUsuarios();
    }

    @GetMapping("/nombre/{nombre}")
    public List<Usuarios> listaUsuariosPorNombre(@PathVariable String nombre) {
        return usuariosService.traerUsuarioPorNombre(nombre);
    }

    @GetMapping("/correo/{correo}")
    public Optional<Usuarios> listaUsuariosPorCorreo(@PathVariable String correo) {
        return usuariosService.traerUsuarioPorCorreo(correo);
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearUsuario(@RequestBody Usuarios usuarios) {
        System.out.println("Petición POST recibida en /growza/usuarios/crear");
        System.out.println("Datos del usuario recibidos: " + usuarios.getCorreo());
        // 1. Establece la fecha de registro automáticamente.
        usuarios.setFecha_registro(LocalDate.now());
        // 2. Delega la creación del usuario al servicio.
        usuariosService.crearUsuario(usuarios);
        return ResponseEntity.ok("Usuario creado con éxito");
    }

    @PostMapping("/loginConDTO")
    public ResponseEntity<?> loginConDTO(@RequestBody UserDto userDto) {
        UserDetails userDetails = usuariosService.loadUserByUsername(userDto.getCorreo());
        if (userDetails != null && passwordEncoder.matches(userDto.getContrasena(), userDetails.getPassword())) {
            String token = jwtUtil.generateToken(userDto.getCorreo());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }
    

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editarUsuario(@PathVariable Long id, @RequestBody Usuarios usuarioActualizado) {
        usuariosService.editarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok("Usuario editado con éxito");
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuariosService.eliminarUsuarios(id);
        return ResponseEntity.ok("Usuario eliminado con éxito");
    }
}