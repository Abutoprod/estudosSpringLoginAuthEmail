package com.example.demo.controller;

import com.example.demo.repository.MovimentacaoCreditoRepository; // Import do repositório novo
import com.example.demo.repository.usuarioRepository;           // Import do seu repositório de usuários
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // Resolve o erro [26,6]
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;                                     // Resolve o erro do BigDecimal [58,41]
import java.util.List;
import com.example.demo.domain.usuario; // Resolve o erro da classe 'usuario'
import com.example.demo.domain.MovimentacaoCredito; // Resolve o erro da classe 'MovimentacaoCredito'
import org.springframework.security.core.annotation.AuthenticationPrincipal;
    
@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    @Autowired
    private MovimentacaoCreditoRepository movimentacaoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @PostMapping("/ajustar")
    @Transactional
    public ResponseEntity ajustarCredito(@RequestBody AjusteCreditoDTO dados) {
        var usuarioOptional = usuarioRepository.findById(dados.usuarioId());

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var usuario = usuarioOptional.get();
        
        // 1. Registra a movimentação no extrato
        var movimentacao = new MovimentacaoCredito();
        movimentacao.setUsuario(usuario);
        movimentacao.setValor(dados.valor());
        movimentacao.setMotivo(dados.motivo());
        movimentacaoRepository.save(movimentacao);

        // 2. Atualiza o saldo real no cadastro do usuário
        // Saldo Atual + Valor (que pode ser negativo)
        BigDecimal novoSaldo = usuario.getCreditos().add(dados.valor());
        
        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("Saldo insuficiente para realizar essa operação.");
        }

        usuario.setCreditos(novoSaldo);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Saldo atualizado! Novo saldo de " + usuario.getNome() + ": R$ " + novoSaldo);
    }
   
        
}

record AjusteCreditoDTO(Long usuarioId, BigDecimal valor, String motivo) {}