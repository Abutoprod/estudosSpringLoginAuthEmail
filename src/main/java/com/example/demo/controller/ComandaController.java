package com.example.demo.controller;

import com.example.demo.domain.Comanda;
import com.example.demo.dto.ComandaResponseDTO;
import com.example.demo.dto.ItemComandaDTO;
import com.example.demo.repository.*;
import org.springframework.security.core.Authentication;
import com.example.demo.domain.ItemComanda;
import com.example.demo.domain.usuario;
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
    private FilialRepository filialRepository; // Injeção necessária

    // 1. ABRIR COMANDA: Agora exige o filialId para vincular a unidade
    @PostMapping("/abrir/{usuarioId}")
    public ResponseEntity abrirComanda(@PathVariable Long usuarioId, @RequestParam Long filialId) {
        var cliente = usuarioRepository.findById(usuarioId);
        var filial = filialRepository.findById(filialId);

        if (cliente.isEmpty()) return ResponseEntity.badRequest().body("Cliente não encontrado.");
        if (filial.isEmpty()) return ResponseEntity.badRequest().body("Filial não encontrada.");

        Comanda comanda = new Comanda();
        comanda.setCliente(cliente.get());
        comanda.setFilial(filial.get()); // Vínculo com a Entidade Filial
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
    @Transactional
    @PostMapping("/{comandaId}/item")
    public ResponseEntity adicionarItem(@PathVariable Long comandaId, @RequestParam String codigoProduto, @RequestParam Integer quantidade) {
        // 1. Busca a comanda
        var comandaOptional = comandaRepository.findById(comandaId);
        if (comandaOptional.isEmpty()) return ResponseEntity.notFound().build();
        var comanda = comandaOptional.get();

        // 2. Busca o produto pelo código
        var produtoOptional = produtoRepository.findByCodigo(codigoProduto);
        if (produtoOptional.isEmpty()) return ResponseEntity.badRequest().body("Produto não encontrado.");
        var produto = produtoOptional.get();

        // --- REGRA DE OURO: VALIDAÇÃO DE FILIAL ---
        // Verificamos se a filial do produto é a mesma da comanda
        if (!produto.getFilial().getId().equals(comanda.getFilial().getId())) {
            return ResponseEntity.status(403).body("Erro: Este produto pertence a outra filial ("
                    + produto.getFilial().getNome() + ") e não pode ser adicionado nesta comanda.");
        }

        // 3. Valida estoque
        if (produto.getQuantidade() < quantidade) {
            return ResponseEntity.badRequest().body("Estoque insuficiente. Disponível: " + produto.getQuantidade());
        }

        // 4. Cria o item da comanda
        ItemComanda item = new ItemComanda();
        item.setComanda(comanda);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPrecoVenda());

        // 5. Atualiza estoque e total da comanda
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        comanda.setValorTotal(comanda.getValorTotal().add(produto.getPrecoVenda().multiply(new BigDecimal(quantidade))));

        comanda.getItens().add(item);
        comandaRepository.save(comanda);

        return ResponseEntity.ok("Item adicionado com sucesso!");
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
    // ADMIN: Listar filtrando obrigatoriamente por Filial
    @GetMapping
    public ResponseEntity listarTodas(
            @RequestParam Long filialId, // Filtro obrigatório
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long clienteId) {

        // Busca as comandas da filial e transforma em Stream para aplicar filtros extras
        var stream = comandaRepository.findByFilialId(filialId).stream();

        if (clienteId != null) {
            stream = stream.filter(c -> c.getCliente().getId().equals(clienteId));
        }

        if ("abertas".equalsIgnoreCase(status)) {
            stream = stream.filter(Comanda::isAberta);
        } else if ("fechadas".equalsIgnoreCase(status)) {
            stream = stream.filter(c -> !c.isAberta());
        }

        List<Comanda> listaFinal = stream.toList();

        if (listaFinal.isEmpty()) {
            return ResponseEntity.ok("Nenhuma comanda encontrada.");
        }

        var response = listaFinal.stream().map(c -> new ComandaResponseDTO(
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