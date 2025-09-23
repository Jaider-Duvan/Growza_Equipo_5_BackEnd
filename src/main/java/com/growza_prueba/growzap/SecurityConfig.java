package com.growza_prueba.growzap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 🔑 Inyecta el filtro directamente. Spring lo creará por nosotros.
    private final JwtRequestFilter jwtRequestFilter;

    // 🔑 Usa el constructor para la inyección de dependencias
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
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
                // 🔑 Añade el filtro de JWT. Instanciamos el filtro aquí mismo.
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔑 El filtro JwtRequestFilter ya no se define como un Bean aquí.
    // En su lugar, debe ser un @Component para que Spring lo gestione.
}