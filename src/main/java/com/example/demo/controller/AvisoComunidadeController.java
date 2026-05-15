package com.example.demo.controller;
import com.example.demo.domain.AvisoComunidade;
import com.example.demo.repository.AvisoComunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/avisos")
public class AvisoComunidadeController {

    @Autowired
    private AvisoComunidadeRepository repository;

    // ENDPOINT DO JOGADOR: Traz apenas o feed ativo
    @GetMapping
    public ResponseEntity<List<AvisoComunidade>> listarAvisosAtivos() {
        List<AvisoComunidade> ativos = repository.findByDataExpiracaoAfterOrderByDataExpiracaoAsc(LocalDateTime.now());
        return ResponseEntity.ok(ativos);
    }

    // ENDPOINT DO ADMIN: Salva um novo aviso no feed
    @PostMapping
    public ResponseEntity<AvisoComunidade> criarAviso(@RequestBody AvisoComunidade novoAviso) {
        // --- ADICIONE ESTA LINHA AQUI ---
        // Força o ID a ser nulo para garantir que o Hibernate trate como uma nova inserção (Gera o IDENTITY no banco)
        novoAviso.setId(null);
        // --------------------------------

        AvisoComunidade salvo = repository.save(novoAviso);
        return ResponseEntity.ok(salvo);
    }

    // ENDPOINT DO ADMIN (OPCIONAL): Permite deletar manualmente se precisar tirar do ar antes do tempo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAviso(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}