package com.example.demo.service;
import com.example.demo.domain.usuario;
import com.example.demo.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
  @Autowired
  private usuarioRepository reporsitory;
  
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JavaMailSender mailSender;

    public usuario registrar(usuario usuario) {
            usuario.setSenha(encoder.encode(usuario.getSenha()));
            usuario.setAtivo(false); 
            usuario.setTokenConfirmacao(UUID.randomUUID().toString()); 
            
            // 1. Salva primeiro e guarda o objeto persistido
            usuario usuarioSalvo = reporsitory.save(usuario);
            
            // 2. Envia o e-mail agora que o usuário já está no banco
            enviarEmailConfirmacao(usuarioSalvo); 
            
            // 3. Por último, retorna o resultado
            return usuarioSalvo;
        }
    private void enviarEmailConfirmacao(usuario usuario) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(usuario.getEmail());
        mensagem.setSubject("Confirmação de Conta");
        mensagem.setText("Olá " + usuario.getNome() + ",\n\n" +
                       "Clique no link abaixo para confirmar sua conta:\n" +
                       "http://localhost:8080/api/usuarios/confirmar?token=" + usuario.getTokenConfirmacao());
        
        mailSender.send(mensagem);
    }

    public boolean confirmarUsuario(String token) {
        // Use o Optional do java.util
        Optional<usuario> optionalUsuario = reporsitory.findByTokenConfirmacao(token);
        
        if (optionalUsuario.isPresent()) {
            usuario usuario = optionalUsuario.get();
            usuario.setAtivo(true);
            usuario.setTokenConfirmacao(null);
            reporsitory.save(usuario);
            return true;
        }
        return false;
    }
}
