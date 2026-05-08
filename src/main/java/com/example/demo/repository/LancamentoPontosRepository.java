package com.example.demo.repository;

import com.example.demo.domain.LancamentoPontos;
import com.example.demo.dto.RankingDTO; // Importando o DTO que vamos criar
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // IMPORTANTE: faltava este
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List; // IMPORTANTE: faltava este
import com.example.demo.dto.DesempenhoMensalDTO;

@Repository
public interface LancamentoPontosRepository extends JpaRepository<LancamentoPontos, Long> {

    // QUERY 1: O Ranking do Mês (Para a aba de competição)
    @Query("""
        SELECT new com.example.demo.dto.RankingDTO(u.id, u.nome, SUM(l.pontos))
        FROM LancamentoPontos l
        JOIN l.usuario u
        WHERE l.jogo.id = :jogoId
        AND MONTH(l.dataLancamento) = MONTH(CURRENT_DATE)
        AND YEAR(l.dataLancamento) = YEAR(CURRENT_DATE)
        GROUP BY u.id, u.nome
        ORDER BY SUM(l.pontos) DESC
    """)
    List<RankingDTO> retornarRankingMensal(@Param("jogoId") Long jogoId);

    // QUERY 2: O Histórico do Jogador (Para o gráfico de 1 ano no perfil)
    @Query("""
        SELECT new com.example.demo.dto.DesempenhoMensalDTO(
            MONTH(l.dataLancamento), 
            YEAR(l.dataLancamento), 
            SUM(l.pontos)
        )
        FROM LancamentoPontos l
        WHERE l.usuario.id = :usuarioId
        AND l.jogo.id = :jogoId
        AND l.dataLancamento >= :umAnoAtras
        GROUP BY YEAR(l.dataLancamento), MONTH(l.dataLancamento)
        ORDER BY YEAR(l.dataLancamento) ASC, MONTH(l.dataLancamento) ASC
    """)
    List<DesempenhoMensalDTO> consultarDesempenhoUltimoAno(
        @Param("usuarioId") Long usuarioId, 
        @Param("jogoId") Long jogoId,
        @Param("umAnoAtras") java.time.LocalDateTime umAnoAtras
    );
}