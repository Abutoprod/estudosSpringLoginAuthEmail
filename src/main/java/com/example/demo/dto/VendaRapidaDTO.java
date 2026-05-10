package com.example.demo.dto;

import java.util.List;

// O DTO principal precisa ser public
public record VendaRapidaDTO(
        Long filialId,
        Long clienteId,
        List<ItemVendaRapidaDTO> itens
) {}

