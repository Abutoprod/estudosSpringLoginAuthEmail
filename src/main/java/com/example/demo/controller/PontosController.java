package com.example.demo.controller;

import com.example.demo.domain.LancamentoPontos;
import com.example.demo.domain.usuario;
import com.example.demo.dto.LancamentoDTO;
import com.example.demo.dto.RankingDTO;
import com.example.demo.dto.DesempenhoMensalDTO;
import com.example.demo.repository.FilialRepository;
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

    @Autowired
    private FilialRepository filialRepository;

    @PostMapping
    @Transactional
    public ResponseEntity lancar(@RequestBody LancamentoDTO dados) {
        var usuario = usuarioRepository.findById(dados.usuarioId());
        var jogo = jogoRepository.findById(dados.jogoId());
        var filial = filialRepository.findById(dados.filialId());

        if (usuario.isEmpty() || jogo.isEmpty() || filial.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário, Jogo ou Filial não encontrado.");
        }

        var lancamento = new LancamentoPontos();
        lancamento.setUsuario(usuario.get());
        lancamento.setJogo(jogo.get());
        lancamento.setFilial(filial.get());
        lancamento.setPontos(dados.pontos());
        lancamento.setObservacao(dados.descricao());

        repository.save(lancamento);
        return ResponseEntity.ok("Pontos lançados com sucesso!");
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingDTO>> consultarRanking(
            @RequestParam Long jogoId,
            @RequestParam Long filialId,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano
    ) {
        if (mes == null) mes = java.time.LocalDate.now().getMonthValue();
        if (ano == null) ano = java.time.LocalDate.now().getYear();

        var ranking = repository.retornarRankingMensalPorFilial(jogoId, filialId, mes, ano);
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/minha-posicao")
    public ResponseEntity<RankingDTO> consultarMinhaPosicao(
            @AuthenticationPrincipal usuario logado,
            @RequestParam Long jogoId,
            @RequestParam Long filialId,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano) {

        if (mes == null) mes = java.time.LocalDate.now().getMonthValue();
        if (ano == null) ano = java.time.LocalDate.now().getYear();

        var ranking = repository.retornarRankingMensalPorFilial(jogoId, filialId, mes, ano);

        for (int i = 0; i < ranking.size(); i++) {
            var item = ranking.get(i);
            if (item.usuarioId().equals(logado.getId())) {
                int posicaoReal = i + 1;

                RankingDTO dtoComPosicao = new RankingDTO(
                        item.usuarioId(),
                        item.nome(),
                        item.totalPontos(),
                        posicaoReal
                );
                return ResponseEntity.ok(dtoComPosicao);
            }
        }
        return ResponseEntity.notFound().build();
    }
} // <--- Fecha a classe do Controller aqui com segurança!