package com.example.demo.controller;

import com.example.demo.dto.EventoDTO;
import com.example.demo.dto.BannerDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/conteudo")
public class ConteudoController {

    @GetMapping("/eventos")
    public List<EventoDTO> listarEventos() {
        return null; // Por enquanto, até implementar a lógica
    }

    @GetMapping("/banners")
    public List<BannerDTO> listarBannersAtivos() {
        return null;
    }
}