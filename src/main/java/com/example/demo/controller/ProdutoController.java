package com.example.demo.controller;

import com.example.demo.domain.Produto;
import com.example.demo.dto.ProdutoDTO;
import com.example.demo.repository.FilialRepository;
import com.example.demo.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;
    @Autowired
    private FilialRepository filialRepository;

    // LISTAR: Qualquer um logado pode ver o estoque (opcional)
    @GetMapping
    public ResponseEntity<List<Produto>> listar(@RequestParam Long filialId) {
        var produtos = repository.findByFilialId(filialId);
        return ResponseEntity.ok(produtos);
    }

    // INSERIR: Somente ADMIN
    // INSERIR: Somente ADMIN
    @PostMapping
    public ResponseEntity criar(@RequestBody ProdutoDTO dados) {
        var filial = filialRepository.findById(dados.filialId())
                .orElseThrow(() -> new RuntimeException("Filial não encontrada"));

        var produto = new Produto();
        produto.setCodigo(dados.codigo());
        produto.setDescricao(dados.descricao());
        produto.setPrecoCompra(dados.precoCompra());
        produto.setPrecoVenda(dados.precoVenda());
        produto.setQuantidade(dados.quantidade());
        produto.setCategoria(dados.categoria());
        produto.setFilial(filial); // Define a filial do produto

        repository.save(produto);
        return ResponseEntity.ok(produto);
    }

    // ALTERAR: Somente ADMIN
    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dados) {
        var produtoOptional = repository.findById(id);
        if (produtoOptional.isEmpty()) return ResponseEntity.notFound().build();

        var produto = produtoOptional.get();
        produto.setDescricao(dados.descricao());
        produto.setPrecoCompra(dados.precoCompra());
        produto.setPrecoVenda(dados.precoVenda());
        produto.setQuantidade(dados.quantidade());

        repository.save(produto);
        return ResponseEntity.ok("Produto atualizado!");
    }

    // DELETAR: Somente ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok("Produto removido!");
    }
    // BUSCAR ESPECÍFICO: Detalhes de um produto
    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}