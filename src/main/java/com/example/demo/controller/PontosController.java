package com.example.demo.controller;

import com.example.demo.domain.LancamentoPontos;
import com.example.demo.domain.usuario;
import com.example.demo.dto.RankingDTO;
import com.example.demo.dto.DesempenhoMensalDTO; // <--- ADICIONE ESTA LINHA
import com.example.demo.repository.LancamentoPontosRepository;
import com.example.demo.repository.usuarioRepository;
import com.example.demo.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pontos")
public class PontosController {

    @Autowired
    private LancamentoPontosRepository repository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private JogoRepository jogoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity lancar(@RequestBody LancamentoDTO dados) {
        var usuario = usuarioRepository.findById(dados.usuarioId());
        var jogo = jogoRepository.findById(dados.jogoId());

        if (usuario.isEmpty() || jogo.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário ou Jogo não encontrado.");
        }

        var lancamento = new LancamentoPontos();
        lancamento.setUsuario(usuario.get());
        lancamento.setJogo(jogo.get());
        lancamento.setPontos(dados.pontos());
        lancamento.setObservacao(dados.descricao());

        repository.save(lancamento);
        return ResponseEntity.ok("Pontos lançados com sucesso!");
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingDTO>> consultarRanking(@RequestParam Long jogoId) {
        var ranking = repository.retornarRankingMensal(jogoId);
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/minha-posicao")
    public ResponseEntity<RankingDTO> consultarMinhaPosicao(
            @AuthenticationPrincipal usuario logado, 
            @RequestParam Long jogoId) {
        
        var ranking = repository.retornarRankingMensal(jogoId);
        
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).usuarioId().equals(logado.getId())) {
                return ResponseEntity.ok(ranking.get(i));
            }
        }
        return ResponseEntity.notFound().build();
    }
} // <--- ESTA CHAVE FECHA A CLASSE. TUDO DEVE ESTAR ACIMA DELA.

record LancamentoDTO(Long usuarioId, Long jogoId, Integer pontos, String descricao) {}