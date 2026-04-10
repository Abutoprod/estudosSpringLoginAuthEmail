package com.example.demo.repository;

import com.example.demo.domain.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
public interface usuarioRepository extends JpaRepository<usuario, Long> {
    // o Spring entende o nome do methodo e cria a query automaticamente, ou seja, ele vai procurar um usuario com o email que for passado como parametro
    // SELECT * FROM usuarios WHERE email = ?
    UserDetails findByEmail(String email);
    
    Optional<usuario> findByTokenConfirmacao(String token);
}
