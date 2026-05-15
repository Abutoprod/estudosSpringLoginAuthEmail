package com.example.demo.repository;

import com.example.demo.domain.AvisoComunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface AvisoComunidadeRepository extends JpaRepository<AvisoComunidade, Long> {

    // Busca apenas avisos onde a data de expiração é MAIOR que o momento atual (ativos)
    List<AvisoComunidade> findByDataExpiracaoAfterOrderByDataExpiracaoAsc(LocalDateTime agora);
    List<AvisoComunidade> findByDataExpiracaoBefore(LocalDateTime agora);
    // Deleta do banco tudo que já venceu
    @Modifying
    @Transactional
    @Query("DELETE FROM AvisoComunidade a WHERE a.dataExpiracao < :agora")
    void deletarAvisosExpirados(LocalDateTime agora);
}