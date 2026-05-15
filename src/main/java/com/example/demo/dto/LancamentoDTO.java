package com.example.demo.dto;

public record LancamentoDTO(
        Long usuarioId,
        Long jogoId,
        Long filialId,
        Integer pontos,
        String descricao
) {}