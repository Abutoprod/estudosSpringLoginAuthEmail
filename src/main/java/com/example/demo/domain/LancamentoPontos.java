
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.demo.domain.Filial;

@Entity
@Table(name = "lancamentos_pontos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LancamentoPontos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private usuario usuario;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    // NOVO: Relacionamento com Filial
    @ManyToOne
    @JoinColumn(name = "filial_id")
    private Filial filial;

    private Integer pontos;
    private LocalDateTime dataLancamento = LocalDateTime.now();
    private String observacao;
}