package com.example.demo.controller;

import com.example.demo.domain.Participante;
import com.example.demo.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participantes")
public class ParticipanteController {

    @Autowired
    private ParticipanteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody Participante participante) {
        if (participante.getCreditos() == null) {
            participante.setCreditos(java.math.BigDecimal.ZERO);
        }
        repository.save(participante);
        return ResponseEntity.ok("Participante cadastrado com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<Participante>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    // MUDAR NOME OU DADOS DO PARTICIPANTE
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody Participante dadosAtualizados) {
        var participante = repository.findById(id);
        
        if (participante.isPresent()) {
            var p = participante.get();
            p.setNome(dadosAtualizados.getNome());
            // Você também pode permitir mudar os créditos por aqui se desejar
            if (dadosAtualizados.getCreditos() != null) {
                p.setCreditos(dadosAtualizados.getCreditos());
            }
            return ResponseEntity.ok("Dados do participante atualizados!");
        }
        
        return ResponseEntity.notFound().build();
    }

    // DELETAR PARTICIPANTE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Participante removido com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }
}