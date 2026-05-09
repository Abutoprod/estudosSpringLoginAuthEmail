package com.example.demo.controller;

import com.example.demo.domain.Evento;
import com.example.demo.domain.Filial;
import com.example.demo.domain.Jogo;
import com.example.demo.dto.EventoDTO;
import com.example.demo.dto.EventoRequestDTO;
import com.example.demo.repository.EventoRepository;
import com.example.demo.repository.FilialRepository;
import com.example.demo.repository.JogoRepository; // Verifique se o nome é este
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoRepository repository;

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private FilialRepository filialRepository;

    @PostMapping
    public ResponseEntity<Evento> criar(@RequestBody EventoRequestDTO dto) {
        // Busca a filial no banco pelo ID que vem no DTO
        Filial filial = filialRepository.findById(dto.filialId())
                .orElseThrow(() -> new RuntimeException("Filial não encontrada"));

        Evento evento = new Evento();
        evento.setTitulo(dto.titulo());
        evento.setDescricao(dto.descricao());
        evento.setDataHora(dto.dataHora());
        evento.setLinkInscricao(dto.linkInscricao());
        evento.setFilial(filial); // Seta o objeto Filial completo
        evento.setUrlImagem("http://localhost:8080/uploads/" + dto.nomeImagem());

        Jogo jogo = jogoRepository.findById(dto.jogoId())
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
        evento.setJogo(jogo);

        return ResponseEntity.ok(repository.save(evento));
    }

    @GetMapping
    public ResponseEntity<List<EventoDTO>> listar() {
        var eventos = repository.findByDataHoraAfterOrderByDataHoraAsc(LocalDateTime.now());

        var dtos = eventos.stream().map(e -> new EventoDTO(
                e.getId(),
                e.getTitulo(),
                e.getDescricao(),
                e.getDataHora().toString(),
                e.getUrlImagem(),
                e.getLinkInscricao(),
                e.getJogo() != null ? e.getJogo().getNome() : "Sem Jogo",
                // Pega o NOME da filial para exibir no JSON
                e.getFilial() != null ? e.getFilial().getNome() : "Sem Filial"
        )).toList();

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} // <--- VERIFIQUE SE ESTA CHAVE ESTÁ AQUI NO FINAL