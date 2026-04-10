package com.example.demo.config;

import com.example.demo.repository.usuarioRepository;
import com.example.demo.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private usuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Tenta recuperar o token que vem no cabeçalho "Authorization"
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            // 2. Valida o token e descobre quem é o dono (o email)
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByEmail(subject);

            // 3. Força a autenticação para o Spring entender que o cara está logado
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 4. Manda a requisição seguir o caminho dela (ir pro Controller)
        filterChain.doFilter(request, response);
    }
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Pula os 7 caracteres de "Bearer "
        }
        return null;
    }
    /*private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer", "");
        }
        return null;
    }*/
}