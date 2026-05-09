package com.example.demo.repository;

import com.example.demo.domain.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    List<Comanda> findByFilialId(Long filialId);
    Comanda findByClienteIdAndFilialIdAndAbertaTrue(Long clienteId, Long filialId);
}