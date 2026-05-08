package com.example.demo.controller;

import com.example.demo.domain.Jogo;
import com.example.demo.repository.JogoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jogos")
public class JogoController {

    @Autowired
    private JogoRepository repository;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody @Valid Jogo jogo) {
        repository.save(jogo);
        return ResponseEntity.ok("Jogo cadastrado com sucesso!");
    }
    
    @GetMapping
    public ResponseEntity listar() {
        return ResponseEntity.ok(repository.findAll());
    }
}