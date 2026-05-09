package com.example.demo.controller;

import com.example.demo.domain.Filial;
import com.example.demo.repository.FilialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filiais")
public class FilialController {

    @Autowired
    private FilialRepository repository;

    // Criar nova filial (Ex: Bauru, Botucatu)
    @PostMapping
    public ResponseEntity<Filial> criar(@RequestBody Filial filial) {
        return ResponseEntity.ok(repository.save(filial));
    }

    // Listar todas as filiais cadastradas
    @GetMapping
    public List<Filial> listar() {
        return repository.findAll();
    }

    // Buscar uma filial específica
    @GetMapping("/{id}")
    public ResponseEntity<Filial> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar filial (Cuidado: Se houver produtos/comandas nela, o banco pode bloquear)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // ALTERAR: Atualiza os dados de uma filial existente
    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody Filial dados) {
        return repository.findById(id)
                .map(filial -> {
                    filial.setNome(dados.getNome());
                    filial.setCidade(dados.getCidade());
                    filial.setEndereco(dados.getEndereco());
                    filial.setAtivo(dados.isAtivo());

                    repository.save(filial);
                    return ResponseEntity.ok("Filial atualizada com sucesso!");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}