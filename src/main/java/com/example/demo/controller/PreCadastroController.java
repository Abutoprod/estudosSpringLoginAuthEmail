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

@RestController
@RequestMapping("/api/eventos")
public class PreCadastroController {

    @Autowired
    private PreCadastroRepository preCadastroRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @PostMapping("/{eventoId}/pre-cadastro")
    @Transactional
    public ResponseEntity<String> realizarPreCadastro(@PathVariable Long eventoId, Authentication authentication) {
        // 1. Pega o usuário logado (Cast para a tua classe 'usuario')
        usuario usuarioLogado = (usuario) authentication.getPrincipal();

        var eventoOptional = eventoRepository.findById(eventoId);
        if (eventoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var evento = eventoOptional.get();

        // 2. Validação de data (CORRIGIDO: usando getDataHora() conforme definido em Evento.java
        if (LocalDateTime.now().isAfter(evento.getDataHora())) { // CORRIGIDO PARA getDataHora()
            return ResponseEntity.badRequest().body("As inscrições para este evento já fecharam.");
        }

        // 3. Validação de duplicidade
        boolean jaInscrito = preCadastroRepository.existsByEventoIdAndUsuarioId(eventoId, usuarioLogado.getId());
        if (jaInscrito) {
            return ResponseEntity.badRequest().body("Você já está pré-cadastrado neste evento.");
        }

        // 4. Salva o pré-cadastro
        var preCadastro = new PreCadastro();
        preCadastro.setEvento(evento);
        preCadastro.setUsuario(usuarioLogado);
        preCadastroRepository.save(preCadastro);

        return ResponseEntity.ok("Pré-cadastro realizado com sucesso!");
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