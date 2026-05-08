package com.example.demo.repository;

import com.example.demo.domain.MovimentacaoCredito;
import com.example.demo.domain.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimentacaoCreditoRepository extends JpaRepository<MovimentacaoCredito, Long> {
    List<MovimentacaoCredito> findAllByUsuario(usuario usuario);
}