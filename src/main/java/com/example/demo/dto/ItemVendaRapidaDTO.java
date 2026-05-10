package com.example.demo.dto;

// O DTO de itens TAMBÉM precisa ser public para o Controller enxergar
public record ItemVendaRapidaDTO(
        String codigoProduto,
        Integer quantidade
) {}
