package com.example.demo.repository;
import com.example.demo.domain.PreCadastro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PreCadastroRepository extends JpaRepository<PreCadastro, Long> {
    List<PreCadastro> findAllByEventoId(Long eventoId);
    boolean existsByEventoIdAndUsuarioId(Long eventoId, Long usuarioId);
    Optional<PreCadastro> findByEventoIdAndUsuarioId(Long eventoId, Long usuarioId);
}