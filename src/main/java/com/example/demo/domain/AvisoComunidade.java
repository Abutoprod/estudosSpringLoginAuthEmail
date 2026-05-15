package com.example.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avisos_comunidade")
public class AvisoComunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000) // Suporta mensagens mais longas
    private String mensagem;

    @Column(name = "url_foto")
    private String urlFoto;

    private String link;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;

    // Construtores
    public AvisoComunidade() {}

    public AvisoComunidade(String titulo, String mensagem, String urlFoto, String link, LocalDateTime dataExpiracao) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.urlFoto = urlFoto;
        this.link = link;
        this.dataExpiracao = dataExpiracao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public String getUrlFoto() { return urlFoto; }
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public LocalDateTime getDataExpiracao() { return dataExpiracao; }
    public void setDataExpiracao(LocalDateTime dataExpiracao) { this.dataExpiracao = dataExpiracao; }
}