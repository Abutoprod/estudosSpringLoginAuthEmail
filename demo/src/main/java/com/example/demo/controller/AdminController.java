package com.example.demo.controller;

import com.example.demo.domain.Jogo;
import com.example.demo.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private JogoRepository jogoRepository;

    // Endpoint para listar todos os jogos (Bom para testar)
    @GetMapping("/jogos")
    public List<Jogo> listarJogos() {
        return jogoRepository.findAll();
    }

    // Endpoint para cadastrar um novo jogo
    // A anotação abaixo garante que SÓ quem tem role ROLE_ADMIN no banco consiga postar
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/jogos")
    public ResponseEntity<Jogo> cadastrarJogo(@RequestBody Jogo jogo) {
        Jogo novoJogo = jogoRepository.save(jogo);
        return ResponseEntity.ok(novoJogo);
    }
}