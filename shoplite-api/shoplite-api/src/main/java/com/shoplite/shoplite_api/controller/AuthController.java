package com.shoplite.shoplite_api.controller;

// Seus pacotes internos (DTOs, Services, Repositories, Entities)
import com.shoplite.shoplite_api.dto.AuthResponse;
import com.shoplite.shoplite_api.dto.LoginRequest;
import com.shoplite.shoplite_api.dto.RegisterRequest;
import com.shoplite.shoplite_api.service.AutenticacaoService;
import com.shoplite.shoplite_api.service.JwtService;
import com.shoplite.shoplite_api.repository.UsuarioRepository;
import com.shoplite.shoplite_api.model.Usuario; // Ou o pacote onde sua entidade Usuario está
import com.shoplite.shoplite_api.model.Papel;   // Ou o pacote onde seu enum/classe Papel está

// Spring HTTP e Web Rest
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Spring Security
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AutenticacaoService autenticacaoService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtService jwtService,
                           AutenticacaoService autenticacaoService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha())); // nunca salvar senha em texto puro
        usuario.setPapel(Papel.CLIENTE);
        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        UserDetails usuario = autenticacaoService.loadUserByUsername(request.email());
        String token = jwtService.gerarToken(usuario);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
