package com.example.demo.controller;

import com.example.demo.domain.Comanda;
import com.example.demo.dto.ComandaResponseDTO;
import com.example.demo.dto.ItemComandaDTO;
import org.springframework.security.core.Authentication;
import com.example.demo.domain.ItemComanda;
import com.example.demo.domain.usuario;
import com.example.demo.repository.ComandaRepository;
import com.example.demo.repository.ItemComandaRepository;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.repository.usuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comandas")
public class ComandaController {

    @Autowired
    private ComandaRepository comandaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;
    @Autowired
    private ItemComandaRepository itemComandaRepository;

    // 1. ABRIR COMANDA (Somente ADMIN via SecurityConfig)
    @PostMapping("/abrir/{usuarioId}")
    public ResponseEntity abrirComanda(@PathVariable Long usuarioId) {
        var cliente = usuarioRepository.findById(usuarioId);
        if (cliente.isEmpty()) return ResponseEntity.badRequest().body("Cliente não encontrado.");

        Comanda comanda = new Comanda();
        comanda.setCliente(cliente.get());
        comanda.setDataAbertura(LocalDateTime.now());
        comanda.setAberta(true);
        comanda.setValorTotal(BigDecimal.ZERO);
        comanda.setItens(new ArrayList<>());

        comandaRepository.save(comanda);
        return ResponseEntity.ok(comanda);
    }

    // Adicione este método ao seu ComandaController.java
    @GetMapping("/minha")
    public ResponseEntity verMinhaComanda(
            Authentication authentication,
            @RequestParam(required = false) String status) {

        usuario usuarioLogado = (usuario) authentication.getPrincipal();

        // Busca todas as comandas do cliente no repositório
        var todasDoCliente = comandaRepository.findAll().stream()
                .filter(c -> c.getCliente().getId().equals(usuarioLogado.getId()));

        // AQUI ESTÁ A CORREÇÃO: Aplicar o filtro do parâmetro "status"
        if ("abertas".equalsIgnoreCase(status)) {
            todasDoCliente = todasDoCliente.filter(Comanda::isAberta);
        } else if ("fechadas".equalsIgnoreCase(status)) {
            todasDoCliente = todasDoCliente.filter(c -> !c.isAberta());
        }

        var comandas = todasDoCliente.toList();

        // Converte para o seu DTO para não vir aquela "bagunça" de informação
        var response = comandas.stream().map(c -> new ComandaResponseDTO(
                c.getId(),
                c.getCliente().getNome(),
                c.getValorTotal(),
                c.isAberta(),
                c.getDataAbertura(),
                c.getItens().stream().map(item -> new ItemComandaDTO(
                        item.getProduto().getDescricao(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                )).toList()
        )).toList();

        return ResponseEntity.ok(response);
    }
    // 2. ADICIONAR ITEM E BAIXAR ESTOQUE (Somente ADMIN via SecurityConfig)
    @PostMapping("/{comandaId}/adicionar-item")
    @Transactional
    public ResponseEntity adicionarItem(@PathVariable Long comandaId, @RequestParam Long produtoId, @RequestParam Integer quantidade) {
        var comandaOptional = comandaRepository.findById(comandaId);
        var produtoOptional = produtoRepository.findById(produtoId);

        if (comandaOptional.isEmpty()) return ResponseEntity.notFound().build();
        if (produtoOptional.isEmpty()) return ResponseEntity.badRequest().body("Produto inexistente.");

        var comanda = comandaOptional.get();
        var produto = produtoOptional.get();

        if (!comanda.isAberta()) return ResponseEntity.badRequest().body("Esta comanda já está fechada.");

        // VALIDAÇÃO DE ESTOQUE
        if (produto.getQuantidade() < quantidade) {
            return ResponseEntity.badRequest().body("Estoque insuficiente. Disponível: " + produto.getQuantidade());
        }

        // BAIXA NO ESTOQUE
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        produtoRepository.save(produto);

        // CRIAÇÃO DO ITEM DA COMANDA
        ItemComanda item = new ItemComanda();
        item.setComanda(comanda);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPrecoVenda());

        // ATUALIZA TOTAL DA COMANDA
        BigDecimal valorItem = produto.getPrecoVenda().multiply(new BigDecimal(quantidade));
        comanda.setValorTotal(comanda.getValorTotal().add(valorItem));
        comanda.getItens().add(item);

        comandaRepository.save(comanda);
        return ResponseEntity.ok("Item adicionado e estoque atualizado!");
    }

    // 3. FECHAR COMANDA (Pagamento)
    @PutMapping("/{comandaId}/fechar")
    public ResponseEntity fecharComanda(@PathVariable Long comandaId) {
        var comandaOptional = comandaRepository.findById(comandaId);
        if (comandaOptional.isEmpty()) return ResponseEntity.notFound().build();

        var comanda = comandaOptional.get();
        comanda.setAberta(false);
        comanda.setDataFechamento(LocalDateTime.now());

        comandaRepository.save(comanda);
        return ResponseEntity.ok("Comanda fechada com sucesso. Total: R$ " + comanda.getValorTotal());
    }

    // ADMIN: Listar comandas com filtro opcional (?status=abertas ou ?status=fechadas)
    @GetMapping
    public ResponseEntity listarTodas(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long clienteId) {

        // 1. Pega todas as comandas do banco
        var stream = comandaRepository.findAll().stream();

        // 2. Filtra por Cliente (se o ID for informado)
        if (clienteId != null) {
            stream = stream.filter(c -> c.getCliente().getId().equals(clienteId));
        }

        // 3. Filtra por Status (se informado)
        if ("abertas".equalsIgnoreCase(status)) {
            stream = stream.filter(Comanda::isAberta);
        } else if ("fechadas".equalsIgnoreCase(status)) {
            stream = stream.filter(c -> !c.isAberta());
        }

        List<Comanda> comandas = stream.toList();

        if (comandas.isEmpty()) {
            return ResponseEntity.ok("Nenhuma comanda encontrada para os filtros aplicados.");
        }

        // 4. Converte para DTO
        var response = comandas.stream().map(c -> new ComandaResponseDTO(
                c.getId(),
                c.getCliente().getNome(),
                c.getValorTotal(),
                c.isAberta(),
                c.getDataAbertura(),
                c.getItens().stream().map(item -> new ItemComandaDTO(
                        item.getProduto().getDescricao(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                )).toList()
        )).toList();

        return ResponseEntity.ok(response);
    }
}