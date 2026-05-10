package com.example.demo.controller;

import com.example.demo.service.UsuarioService;
import com.example.demo.domain.usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.UserRole;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.domain.MovimentacaoCredito; // Resolve o erro [45,32]
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Resolve os erros [39,66] e [45,75]
import java.math.BigDecimal; // Resolve o erro [53,69]
import java.util.List;
import com.example.demo.repository.MovimentacaoCreditoRepository; // Resolve o erro [26,6]
import com.example.demo.repository.LancamentoPontosRepository; // Resolve o erro [27,13]
import com.example.demo.dto.DesempenhoMensalDTO;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService service;

    @Autowired
    private MovimentacaoCreditoRepository movimentacaoRepository;
    
    @Autowired
    private LancamentoPontosRepository lancamentoRepository;
    
    @PostMapping("/registrar")
    public String registrarUsuario(@RequestBody usuario usuario) 
    {
        usuario.setRole(UserRole.USER);
        usuario.setCreditos(java.math.BigDecimal.ZERO);

       service.registrar(usuario);
       return "Usuário registrado com sucesso! Verifique seu e-mail para confirmar a conta.";
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoUsuario>> listarTodos() {

        var lista = service.listarTodos().stream()
                .map(DadosDetalhamentoUsuario::new)
                .toList();
        return ResponseEntity.ok(lista);
    }


    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmar(@RequestParam String token) {
        boolean confirmado = service.confirmarUsuario(token);
       if (confirmado) {
        return ResponseEntity.ok("Conta confirmada com sucesso! Agora você pode fazer login.");
       }
       else {
        return ResponseEntity.badRequest().body("Token de confirmação inválido ou expirado.");
       }
    }
    @GetMapping("/me")
    public ResponseEntity<DadosDetalhamentoUsuario> consultarMe(@AuthenticationPrincipal usuario logado) {
        // Retorna o DTO com os dados básicos e o saldo atual
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(logado));
    }

    @GetMapping("/meu-extrato")
    public ResponseEntity<List<MovimentacaoCredito>> consultarMeuExtrato(@AuthenticationPrincipal usuario logado) {
        // Busca o histórico de créditos do usuário logado
        var extrato = movimentacaoRepository.findAllByUsuario(logado);
        return ResponseEntity.ok(extrato);
    }
    @GetMapping("/meu-desempenho")
        public ResponseEntity<List<DesempenhoMensalDTO>> consultarMeuDesempenho(
                @AuthenticationPrincipal usuario logado, 
                @RequestParam Long jogoId) {
            
            // Calcula a data de 12 meses atrás
            var umAnoAtras = java.time.LocalDateTime.now().minusMonths(12);
            
            // Busca os dados somados no banco
            var dadosGrafico = lancamentoRepository.consultarDesempenhoUltimoAno(
                    logado.getId(), 
                    jogoId, 
                    umAnoAtras
            );
            
            return ResponseEntity.ok(dadosGrafico);
        }
}

// DTO para não enviar a senha e outros dados sensíveis para o App
record DadosDetalhamentoUsuario(Long id, String nome, String email, BigDecimal creditos, String role) {
    public DadosDetalhamentoUsuario(usuario usuario) {
        this(usuario.getId(), 
             usuario.getNome(), 
             usuario.getEmail(), 
             usuario.getCreditos(), 
             usuario.getRole().toString());
    }

}
