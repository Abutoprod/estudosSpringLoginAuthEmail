package com.example.demo.repository;

import com.example.demo.domain.LancamentoPontos;
import com.example.demo.dto.RankingDTO;
import com.example.demo.dto.DesempenhoMensalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface LancamentoPontosRepository extends JpaRepository<LancamentoPontos, Long> {

    // QUERY 1 ALTERADA: Agora aceita :mes e :ano.
    // Se :mes for 0 ou nulo, ela traz o acumulado histórico (opcional).
    @Query("""
        SELECT new com.example.demo.dto.RankingDTO(u.id, u.nome, SUM(l.pontos))
        FROM LancamentoPontos l
        JOIN l.usuario u
        WHERE l.jogo.id = :jogoId
        AND l.filial.id = :filialId
        AND (:mes IS NULL OR MONTH(l.dataLancamento) = :mes)
        AND (:ano IS NULL OR YEAR(l.dataLancamento) = :ano)
        GROUP BY u.id, u.nome
        ORDER BY SUM(l.pontos) DESC
    """)
    List<RankingDTO> retornarRankingMensalPorFilial(
            @Param("jogoId") Long jogoId,
            @Param("filialId") Long filialId,
            @Param("mes") Integer mes,
            @Param("ano") Integer ano
    );

    // QUERY 2: Histórico do Jogador (Mantida)
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
            @Param("umAnoAtras") LocalDateTime umAnoAtras
    );
}