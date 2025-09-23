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

@Component // Spring lo gestiona como bean
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UsuariosService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 🔎 Depuración: imprime si llega el header
        if (authorizationHeader == null) {
            System.out.println("❌ No se encontró header Authorization en la request: " + request.getRequestURI());
        } else {
            System.out.println("📩 Header Authorization recibido: " + authorizationHeader);
        }

        // Valida si el header tiene formato Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractCorreo(jwt); // tu método para extraer el correo
                System.out.println("✅ Usuario extraído del JWT: " + username);
            } catch (Exception e) {
                System.out.println("⚠️ Error al extraer usuario del token: " + e.getMessage());
            }
        }

        // Si hay usuario y el contexto de seguridad está vacío
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) { // valida que el token sea correcto
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

        // Continua con el resto de filtros
        chain.doFilter(request, response);
    }
}
