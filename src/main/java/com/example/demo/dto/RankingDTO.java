package com.example.demo.dto;

public record RankingDTO(Long usuarioId, String nome, Long totalPontos, Integer posicao) {

    // CONSTRUTOR AUXILIAR: O Hibernate usará este aqui automaticamente ao rodar a query com 3 campos!
    public RankingDTO(Long usuarioId, String nome, Long totalPontos) {
        this(usuarioId, nome, totalPontos, 0); // Define a posição padrão inicial como 0
    }
}