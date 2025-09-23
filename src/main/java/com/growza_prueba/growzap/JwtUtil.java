package com.growza_prueba.growzap;

// Importaciones estándar para manejar tokens, claims (información) y la firma.
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
// Clases de Spring para inyección de valores y marcar la clase como componente.
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

// @Component: Marca esta clase para que Spring la gestione e inyecte (buena práctica).
@Component
public class JwtUtil {

    // @Value: Inyecta la clave secreta desde el archivo de configuración (esencial para seguridad).
    @Value("${jwt.secret}")
    private String secretKey; // Clave usada para firmar y verificar tokens.

    // --- Generación y Creación de Token ---

    public String generateToken(String correo) {
        return Jwts.builder()
                .setSubject(correo) // Aquí se usa el correo como el "sujeto" del token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    // --- Extracción de Información ---

    public String extractCorreo(String token) {
        // Se puede renombrar el método para mayor claridad
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Método genérico para obtener cualquier dato (claim).
        final Claims claims = extractAllClaims(token); // Obtiene todos los datos verificados.
        return claimsResolver.apply(claims); // Aplica la función para obtener el dato específico.
    }

    private Claims extractAllClaims(String token) {
        // Analiza el token y verifica la firma con la clave secreta.
        // Si el token es inválido o alterado, lanza una excepción de seguridad.
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // --- Validación de Token ---

    public boolean isTokenExpired(String token) {
        // Verifica si la fecha de expiración es anterior a la fecha actual.
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        // Usa el método genérico para obtener la fecha de expiración.
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean validateToken(String jwt, UserDetails userDetails) {
        return false;
    }
}