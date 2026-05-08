package com.example.demo.domain;

import java.util.Collection;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String senha;

    private boolean ativo = false; 

    private String tokenConfirmacao;

    // NOVO: Define se o usuário é ADMIN ou USER
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;


    // NOVO: Sistema de créditos (usando BigDecimal para precisão monetária/pontos)
    private BigDecimal creditos = BigDecimal.valueOf(0.0);

    @Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    // Se no seu banco o campo role for ADMIN, 
    // aqui você entrega para o Spring como ROLE_ADMIN
    if (this.role == UserRole.ADMIN) {
        return List.of(
            new SimpleGrantedAuthority("ROLE_ADMIN"), 
            new SimpleGrantedAuthority("ROLE_USER")
        );
    }
    
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
}

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

    @Override
    public boolean isAccountNonExpired() { 
        return true; 
    }
    
    @Override
    public boolean isAccountNonLocked() { 
        return true; 
    }
    
    @Override
    public boolean isCredentialsNonExpired() { 
        return true; 
    }
}