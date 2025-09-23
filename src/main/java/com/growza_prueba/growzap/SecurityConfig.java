package com.growza_prueba.growzap;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .authorizeHttpRequests(auth -> auth
                        // ➡️ APLICA LA SOLUCIÓN AQUÍ
                        .requestMatchers(HttpMethod.POST, "/growza/usuarios/crear").permitAll()
                        .requestMatchers(HttpMethod.POST, "/growza/usuarios/loginConDTO").permitAll()
                        .requestMatchers("/growza/productos/**", "/growza/categorias/**", "/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("❌ Bloqueado por Spring Security - No autenticado");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autenticado");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("❌ Bloqueado por Spring Security - Acceso denegado");
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                        })
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
