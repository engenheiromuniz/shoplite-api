package com.shoplite.shoplite_api.config;

import com.shoplite.shoplite_api.service.AutenticacaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; 
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Habilitar acesso aos métodos  
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AutenticacaoService autenticacaoService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AutenticacaoService autenticacaoService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.autenticacaoService = autenticacaoService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() 
                
                // Liberação das Rotas do Swagger UI
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Liberação Pública do Console do Banco H2
                .requestMatchers("/h2-console/**").permitAll()
                
                // Regras de negócio existentes: Produtos
                .requestMatchers(HttpMethod.GET, "/produtos/**").permitAll() 
                .requestMatchers(HttpMethod.POST, "/produtos/**").hasAnyRole("VENDEDOR", "ADMIN") 
                
                // INCLUSÃO: Regras para a rota de Categorias
                .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll() // Qualquer um lista as categorias
                .requestMatchers(HttpMethod.POST, "/categorias/**").hasAnyRole("VENDEDOR", "ADMIN") // Só admin/vendedor cria categorias
                
                // Regras de negócio existentes: Pedidos
                .requestMatchers("/pedidos/**").hasRole("CLIENTE") 
                
                .anyRequest().authenticated()
            )
            // Permite que o H2 Console exiba seus frames visuais corretamente
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
