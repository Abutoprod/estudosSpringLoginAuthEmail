
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes_creditos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MovimentacaoCredito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private usuario usuario;

    private BigDecimal valor; // Positivo para acréscimo, Negativo para decréscimo
    private LocalDateTime data = LocalDateTime.now();
    private String motivo; // Ex: "Premiação Semanal", "Compra de Booster", "Correção"
}