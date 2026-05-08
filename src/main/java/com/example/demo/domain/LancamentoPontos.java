
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lancamentos_pontos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LancamentoPontos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private usuario usuario; // Quem ganhou os pontos

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo; // Qual jogo foi (Pokémon, Magic...)

    private Integer pontos;
    private LocalDateTime dataLancamento = LocalDateTime.now();
    
    private String observacao; // Ex: "Semanal 08/05 - 1º Lugar"
}