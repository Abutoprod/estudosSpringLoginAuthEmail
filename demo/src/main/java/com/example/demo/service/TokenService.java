package com.example.demo.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value; // Faltava esse import!
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.domain.usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    
    public String gerarToken(usuario user) { // Mudei para user para ficar mais claro
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Voll.med")
                    .withSubject(user.getEmail())
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret); 
            return JWT.require(algoritmo)
                    .withIssuer("API Voll.med")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            // Esse print vai aparecer no seu log do Docker se o token falhar!
            System.out.println("ERRO JWT: " + exception.getMessage());
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    } // Faltava fechar essa chave aqui!

    private Instant dataExpiracao() {
        // Coloquei 100 horas para matar de vez o erro de fuso horário do Docker
        return LocalDateTime.now().plusHours(100).toInstant(ZoneOffset.of("-03:00"));
    }
}