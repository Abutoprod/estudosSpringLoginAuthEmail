package com.example.demo.controller;

import com.example.demo.domain.Evento;
import com.example.demo.domain.Filial;
import com.example.demo.dto.EventoDTO;
import com.example.demo.dto.EventoRequestDTO; // O Record que criamos por último
import com.example.demo.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa o @RequestBody e @PostMapping
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoRepository repository;

    @PostMapping
public ResponseEntity<Evento> criar(@RequestBody EventoRequestDTO dto) {
    Evento evento = new Evento();
    evento.setTitulo(dto.titulo());
    evento.setDescricao(dto.descricao());
    evento.setDataHora(dto.dataHora());
    evento.setLinkInscricao(dto.linkInscricao());
    evento.setFilial(Filial.valueOf(dto.filial().toUpperCase()));
    
    // Aqui montamos a URL completa para o banco
    evento.setUrlImagem("http://localhost:8080/uploads/" + dto.nomeImagem());

    // Buscar o jogo (Assumindo que você já tem o JogoRepository injetado)
    // Jogo jogo = jogoRepository.findById(dto.jogoId()).orElseThrow();
    // evento.setJogo(jogo);

    return ResponseEntity.ok(repository.save(evento));
}

    @GetMapping
public ResponseEntity<List<EventoDTO>> listar() {
    var eventos = repository.findByDataHoraAfterOrderByDataHoraAsc(LocalDateTime.now());
    
    var dtos = eventos.stream().map(e -> new EventoDTO(
            e.getId(),
            e.getTitulo(),
            e.getDescricao(),
            e.getDataHora().toString(), // Converte LocalDateTime para String
            e.getUrlImagem(),
            e.getLinkInscricao(),
            e.getJogo().getNome(),       // Nome do Jogo
            e.getFilial().name()        // Nome da Filial (O 8º campo!)
    )).toList(); // Se der erro no .toList(), use .collect(Collectors.toList())

    return ResponseEntity.ok(dtos);
}
}