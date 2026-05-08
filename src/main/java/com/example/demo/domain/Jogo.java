
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jogos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Jogo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome; // Ex: Magic The Gathering
    private String categoria; // TCG
}