package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo; // EAN / Código de barras

    private String descricao;
    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private Integer quantidade;
    private String categoria;
}