package com.example.demo.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.domain.usuario;


import org.springframework.beans.factory.annotation.Value;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(usuario username) {
        // Aqui você pode usar uma biblioteca como JWT para criar um token real
        // Por simplicidade, vamos apenas retornar uma string simulada

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // Use a chave secreta para assinar o token
            return JWT.create()
                    .withIssuer("auth-api") // Define o emissor do token
                    .withSubject(username.getId().toString()) // Define o assunto do token como o ID do usuário
                    .withExpiresAt(dataExpiracao()) // Define a expiração para 1 hora
                    .sign(algorithm); // Assina o token com o algoritmo e a chave secreta
        }  catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }
            public String getSubject(String tokenJWT) {
            try {
                var algoritmo = Algorithm.HMAC256(secret);
                return JWT.require(algoritmo)
                        .withIssuer("API estoque.com")
                        .build()
                        .verify(tokenJWT)
                        .getSubject(); // Esse método do JWT retorna o ID que gravamos no subject
            } catch (JWTVerificationException exception) {
                throw new RuntimeException("Token JWT inválido ou expirado!");
            }
        }

    public Instant dataExpiracao() { //Instant é uma classe do Java 8 que representa um ponto no tempo, geralmente usado para datas e horas
       return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")); // Define a expiração para 1 hora a partir do momento atual
    }
    
}
