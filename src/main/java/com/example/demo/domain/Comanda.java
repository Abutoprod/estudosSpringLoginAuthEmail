package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comandas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private usuario cliente; // O cliente dono da comanda

    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    private boolean aberta = true;
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL)
    private List<ItemComanda> itens;

    @ManyToOne
    @JoinColumn(name = "filial_id")
    private Filial filial;
}