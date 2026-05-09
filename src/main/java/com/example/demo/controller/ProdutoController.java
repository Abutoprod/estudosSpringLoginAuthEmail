package com.example.demo.controller;

import com.example.demo.domain.Produto;
import com.example.demo.dto.ProdutoDTO;
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

    // LISTAR: Qualquer um logado pode ver o estoque (opcional)
    @GetMapping
    public List<Produto> listar() {
        return repository.findAll();
    }

    // INSERIR: Somente ADMIN
    // INSERIR: Somente ADMIN
    @PostMapping
    public ResponseEntity criar(@RequestBody ProdutoDTO dados) {
        var produto = new Produto();
        produto.setCodigo(dados.codigo());
        produto.setDescricao(dados.descricao()); // Se enviar "nome" no JSON, aqui fica null
        produto.setPrecoCompra(dados.precoCompra());
        produto.setPrecoVenda(dados.precoVenda());
        produto.setQuantidade(dados.quantidade());
        produto.setCategoria(dados.categoria());

        repository.save(produto);

        // MUDANÇA AQUI: Retorne o objeto 'produto' para conferir os dados no Postman
        return ResponseEntity.ok(produto);
    }

    // ALTERAR: Somente ADMIN
    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dados) {
        var produtoOptional = repository.findById(id);
        if (produtoOptional.isEmpty()) return ResponseEntity.notFound().build();

        var produto = produtoOptional.get();
        produto.setDescricao(dados.descricao());
        produto.setPrecoVenda(dados.precoVenda());
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
        var produto = repository.findById(id);

        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(produto.get());
    }
}