package com.example.demo.repository;

import com.example.demo.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    java.util.Optional<Produto> findByCodigo(String codigo);

    // NOVO: Busca apenas os produtos de uma filial específica
    List<Produto> findByFilialId(Long filialId);
}