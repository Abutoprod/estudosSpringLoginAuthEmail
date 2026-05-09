package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record ComandaResponseDTO(
        Long id,
        String nomeCliente,
        BigDecimal valorTotal,
        boolean aberta,
        LocalDateTime dataAbertura,
        List<ItemComandaDTO> itens
) {}

// DTO auxiliar para os itens não virem "sujos"
