package com.example.demo.dto;
import java.time.LocalDateTime;

public record EventoRequestDTO(
        String titulo,
        String descricao,
        LocalDateTime dataHora,
        String nomeImagem,
        String linkInscricao,
        Long filialId, // Mudamos de String para Long para receber o ID da filial
        Long jogoId
) {}