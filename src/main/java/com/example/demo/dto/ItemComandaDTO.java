package com.example.demo.dto;
import java.math.BigDecimal;
public record ItemComandaDTO(Long id,String produtoNome, Integer quantidade,BigDecimal precoCompra, BigDecimal precoUnitario) {}