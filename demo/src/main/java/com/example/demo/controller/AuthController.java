package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.domain.usuario;
import com.example.demo.service.TokenService;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // o "chefe" da autenticação, ele vai chamar o UserDetailsService para carregar o usuário e o PasswordEncoder para comparar as senhas

    @Autowired
    private TokenService tokenService; // nosso serviço de token, que gera e valida os tokens JWT

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody DadosAutenticacao dados) {
        // 1. transformar os dados de login em um token de autenticação do Spring Security
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        
        var authentication = authenticationManager.authenticate(authenticationToken); // 2. autenticar o usuário (vai chamar o UserDetailsService e o PasswordEncoder)

      // AQUI ESTAVA O ERRO: Transformamos o resultado no seu "Usuario"
        var tokenJWT = tokenService.gerarToken((usuario) authentication.getPrincipal());

        // Devolvemos o token dentro de um objeto simples (DTO)
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
     }   
    
}
 // Estes records podem ficar no final do arquivo ou em arquivos separados
        record DadosAutenticacao(String email, String senha) {}
        record DadosTokenJWT(String token) {}