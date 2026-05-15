package com.example.demo.controller;

import com.example.demo.domain.PreCadastro;
import com.example.demo.domain.usuario; // Tua classe com 'u' minúsculo
import com.example.demo.repository.PreCadastroRepository;
import com.example.demo.repository.EventoRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication; // CORRIGIDO: Import do Spring Security
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
public class PreCadastroController {

    @Autowired
    private PreCadastroRepository preCadastroRepository;

    @Autowired
    private EventoRepository eventoRepository;

    // --- MANTÉM TUA LÓGICA ORIGINAL COM AS VALIDAÇÕES ---
    @PostMapping("/{eventoId}/pre-cadastro")
    @Transactional
    public ResponseEntity<String> realizarPreCadastro(@PathVariable Long eventoId, Authentication authentication) {
        usuario usuarioLogado = (usuario) authentication.getPrincipal();

        var eventoOptional = eventoRepository.findById(eventoId);
        if (eventoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var evento = eventoOptional.get();

        // Validação 1: Data (Inscrições fecham quando o evento começa)
        if (LocalDateTime.now().isAfter(evento.getDataHora())) {
            return ResponseEntity.badRequest().body("As inscrições para este evento já fecharam.");
        }

        // Validação 2: Duplicidade
        boolean jaInscrito = preCadastroRepository.existsByEventoIdAndUsuarioId(eventoId, usuarioLogado.getId());
        if (jaInscrito) {
            return ResponseEntity.badRequest().body("Você já está pré-cadastrado neste evento.");
        }

        var preCadastro = new PreCadastro();
        preCadastro.setEvento(evento);
        preCadastro.setUsuario(usuarioLogado);
        preCadastroRepository.save(preCadastro);

        return ResponseEntity.ok("Pré-cadastro realizado com sucesso!");
    }

    // --- NOVO: ENDPOINT PARA O BOTÃO "SAIR" ---
    @DeleteMapping("/{eventoId}/cancelar-pre-cadastro")
    @Transactional
    public ResponseEntity<String> cancelarPreCadastro(@PathVariable Long eventoId, Authentication authentication) {
        usuario usuarioLogado = (usuario) authentication.getPrincipal();

        // Busca a inscrição específica
        var preCadastro = preCadastroRepository.findByEventoIdAndUsuarioId(eventoId, usuarioLogado.getId());

        if (preCadastro.isPresent()) {
            preCadastroRepository.delete(preCadastro.get());
            return ResponseEntity.ok("Inscrição removida.");
        }

        return ResponseEntity.badRequest().body("Você não está inscrito neste evento.");
    }

    // --- ATUALIZADO: LISTAR PARTICIPANTES COM ID (Para o Android comparar) ---
    @GetMapping("/{eventoId}/participantes-pre-cadastrados_id")
    public ResponseEntity<List<Map<String, Object>>> listarParticipantesDetalhados(@PathVariable Long eventoId) {
        var lista = preCadastroRepository.findAllByEventoId(eventoId);

        List<Map<String, Object>> participantes = lista.stream()
                .map(pc -> {
                    Map<String, Object> dados = new java.util.HashMap<>();
                    dados.put("id", pc.getUsuario().getId());
                    dados.put("nome", pc.getUsuario().getNome());
                    return dados;
                }).toList();

        return ResponseEntity.ok(participantes);
    }



    @GetMapping("/{eventoId}/participantes-pre-cadastrados")
    public ResponseEntity<List<String>> listarNomesPreCadastrados(@PathVariable Long eventoId) {
        var lista = preCadastroRepository.findAllByEventoId(eventoId);

        // Pega o nome via Getter gerado pelo Lombok na classe usuario
        List<String> nomes = lista.stream()
                .map(pc -> pc.getUsuario().getNome())
                .toList();

        return ResponseEntity.ok(nomes);
    }
}