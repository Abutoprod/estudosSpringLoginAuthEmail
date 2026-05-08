package com.example.demo.dto;
import java.time.LocalDateTime;

public record EventoRequestDTO(
    String titulo,
    String descricao,
    LocalDateTime dataHora,
    String nomeImagem,
    String linkInscricao,
    String filial,
    Long jogoId
) {}