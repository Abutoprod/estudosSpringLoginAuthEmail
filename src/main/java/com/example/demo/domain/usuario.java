package com.example.demo.domain;
import java.util.Collection;
import java.util.List;

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
public class usuario implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String senha; // Em um projeto real, a senha deve ser armazenada de forma segura (hash)

    private boolean ativo = false; // Comeca como inativo, só ativa depois de confirmar o email

    private String tokenConfirmacao; // codigo que vai no email para confirmar a conta do usuario

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // Todos os usuários têm a role "USER"
        }
    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email; // O Spring pergunta: "Qual o login?" Você responde: "É o e-mail"
    }

    @Override
    public boolean isEnabled() {
        return ativo; // AQUI: Se 'ativo' for false, o Spring não deixa logar!
    }

    // Esses outros 3 você pode deixar retornando true por enquanto
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
}
