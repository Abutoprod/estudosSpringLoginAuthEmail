package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
    
@Entity
@Table(name = "participantes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Participante {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome; // O Nickname que aparecerá no Ranking
    
    // BigDecimal para créditos pode ficar aqui ou no Usuario. 
    // Se ficar aqui, o Admin pode dar crédito mesmo para quem não tem login.
    private BigDecimal creditos = BigDecimal.ZERO; 
}