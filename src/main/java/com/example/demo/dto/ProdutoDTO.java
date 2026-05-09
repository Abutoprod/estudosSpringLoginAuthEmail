package com.example.demo.dto;

import java.math.BigDecimal;

public record ProdutoDTO(
        String codigo,
        String descricao,
        BigDecimal precoCompra,
        BigDecimal precoVenda,
        Integer quantidade,
        String categoria,
        Long filialId
) {}