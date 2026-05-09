package com.example.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // DESABILITA o CSRF (Obrigatório para testar POST)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/registrar").permitAll() // Libera o registro
                .requestMatchers(HttpMethod.GET, "/api/usuarios/confirmar").permitAll()  // Libera a confirmação
                .requestMatchers(HttpMethod.GET, "/api/participantes").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/jogos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/participantes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/participantes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/participantes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/pontos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/creditos/ajustar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/*/vincular-participante/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/eventos", "/api/eventos/**").permitAll()     
                .requestMatchers(HttpMethod.GET, "/api/conteudo/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll() // LIBERE AS FOTOS
                .requestMatchers(HttpMethod.POST, "/api/eventos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/arquivos/upload").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/eventos/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/estoque").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/estoque/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/estoque/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/estoque").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/estoque/**").permitAll()

                    // comandas
                    .requestMatchers(HttpMethod.POST, "/api/comandas/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/comandas/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/comandas/minha").authenticated() // Cliente vê a dele
                    .requestMatchers(HttpMethod.GET, "/api/comandas/**").hasRole("ADMIN") // Admin vê todas
                .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
        
}
