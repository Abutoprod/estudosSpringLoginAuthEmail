package com.example.demo.repository;

import com.example.demo.domain.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    Comanda findByClienteIdAndAbertaTrue(Long usuarioId);
}