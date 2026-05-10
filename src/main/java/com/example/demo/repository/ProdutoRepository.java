package com.example.demo.repository;

import com.example.demo.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // ESTE É O CORRETO (Provavelmente o campo na sua classe Produto se chama 'codigo')
    Optional<Produto> findByCodigo(String codigo);

    List<Produto> findByFilialId(Long filialId);

    // REMOVA A LINHA: Optional<Produto> findByCode(String code); <- ELA CAUSA O ERRO
}