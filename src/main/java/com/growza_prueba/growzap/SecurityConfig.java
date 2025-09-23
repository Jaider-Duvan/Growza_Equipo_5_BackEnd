package com.growza_prueba.growzap;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/growza/usuarios/crear",
                                "/growza/usuarios/loginConDTO",
                                "/growza/productos/**",
                                "/growza/categorias/**"
                        ).permitAll()
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
