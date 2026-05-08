package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    private LocalDateTime dataHora;
    private String urlImagem; 
    private String linkInscricao;

    @Enumerated(EnumType.STRING) // Salva "BAURU" ou "BOTUCATU" no banco
    private Filial filial;

    @ManyToOne
    private Jogo jogo; 
}