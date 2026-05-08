package com.example.demo.repository;

import com.example.demo.domain.Evento;
import com.example.demo.domain.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    // Lista tudo ordenado
    List<Evento> findByDataHoraAfterOrderByDataHoraAsc(LocalDateTime data);

    // Filtra por filial específica (Ex: Só Bauru)
    List<Evento> findByFilialAndDataHoraAfterOrderByDataHoraAsc(Filial filial, LocalDateTime data);
}