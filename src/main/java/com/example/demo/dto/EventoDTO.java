package com.example.demo.dto;

public record EventoDTO(
        Long id,
        String titulo,
        String descricao,
        String dataHora,
        String urlImagem,
        String linkInscricao,
        String jogoNome,
        String filialNome // Aqui devolvemos o texto "Bauru" ou "Botucatu"
) {}