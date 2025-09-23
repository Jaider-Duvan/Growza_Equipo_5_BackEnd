package com.growza_prueba.growzap;

import com.growza_prueba.growzap.service.UsuariosService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UsuariosService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String requestPath = request.getRequestURI();
        final String requestMethod = request.getMethod();

        // ➡️ Rutas públicas exactas que deben ser ignoradas
        List<String> exactPublicPaths = Arrays.asList(
                "/growza/usuarios/crear",
                "/growza/usuarios/loginConDTO"
        );

        // ➡️ Rutas públicas que pueden tener subrutas
        List<String> prefixPublicPaths = Arrays.asList(
                "/growza/productos",
                "/growza/categorias",
                "/uploads"
        );

        // Comprueba si la ruta actual debe ser ignorada
        if (exactPublicPaths.contains(requestPath) ||
                prefixPublicPaths.stream().anyMatch(requestPath::startsWith)) {

            System.out.println("✅ Petición a ruta pública, saltando filtro JWT: " + requestPath);
            chain.doFilter(request, response);
            return;
        }

        // El resto de la lógica del filtro para validar el token JWT
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractCorreo(jwt);
                System.out.println("✅ Usuario extraído del JWT: " + username);
            } catch (Exception e) {
                System.out.println("⚠️ Error al extraer usuario del token: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("🔐 Usuario autenticado en SecurityContext: " + username);
                } else {
                    System.out.println("❌ Token JWT inválido para el usuario: " + username);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error en validación del usuario/token: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}