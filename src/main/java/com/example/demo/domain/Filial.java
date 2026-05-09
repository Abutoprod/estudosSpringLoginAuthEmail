package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "filiais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cidade;
    private String endereco;
    private boolean ativo = true;
}
/*
public enum Filial {
    BAURU,
    BOTUCATU
}*/