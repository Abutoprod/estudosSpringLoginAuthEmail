package com.example.demo.controller;

import com.example.demo.service.UsuarioService;
import com.example.demo.domain.usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService service;

       
    @PostMapping("/registrar")
    public String registrarUsuario(@RequestBody usuario usuario) 
    {
       service.registrar(usuario);
       return "Usuário registrado com sucesso! Verifique seu e-mail para confirmar a conta.";
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
}
