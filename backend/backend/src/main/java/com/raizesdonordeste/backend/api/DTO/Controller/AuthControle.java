package com.raizesdonordeste.backend.api.DTO.Controller;


import com.raizesdonordeste.backend.api.DTO.Response.AutenticacaoDTO;
import com.raizesdonordeste.backend.api.DTO.Response.LoginResponseDTO;
import com.raizesdonordeste.backend.api.DTO.Response.RegistroDTO;
import com.raizesdonordeste.backend.aplicacao.AuditoriaServico;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.infra.Usuario_Repositorio;
import com.raizesdonordeste.backend.aplicacao.TokenServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControle {

    private static final Logger log = LoggerFactory.getLogger(AuthControle.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Usuario_Repositorio repository;

    @Autowired
    private TokenServico tokenServico;

    @Autowired
    private AuditoriaServico auditoriaServico;

    @Operation(summary = "Login", description = "Autentica o usuário e retorna um token JWT válido por 2 horas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token JWT gerado"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AutenticacaoDTO dto) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenServico.gerarToken((Usuario) auth.getPrincipal());

            auditoriaServico.registrar("LOGIN_SUCESSO", dto.login(), "autenticação realizada");

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (BadCredentialsException e) {
            // Auditamos tentativas falhas para detectar ataques de força bruta
            auditoriaServico.registrar("LOGIN_FALHA", dto.login(), "credenciais inválidas");
            log.warn("[SEGURANÇA] Tentativa de login falha para usuario={}", dto.login());
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistroDTO dto) {
        if (this.repository.findByLogin(dto.login()) != null) return ResponseEntity.badRequest().build();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(dto.senha());
        Usuario novoUsuario = new Usuario(null, dto.login(), senhaCriptografada, dto.role(), null);
        this.repository.save(novoUsuario);

        auditoriaServico.registrar("USUARIO_CRIADO", dto.login(), "cargo=" + dto.role());

        return ResponseEntity.ok().build();
    }
}
