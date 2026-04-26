package com.raizesdonordeste.backend.api.DTO.Controller;


import com.raizesdonordeste.backend.api.DTO.Response.AutenticacaoDTO;
import com.raizesdonordeste.backend.api.DTO.Response.LoginResponseDTO;
import com.raizesdonordeste.backend.api.DTO.Response.RegistroDTO;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.infra.Usuario_Repositorio;
import com.raizesdonordeste.backend.servico.TokenServico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControle {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Usuario_Repositorio repository;

    @Autowired
    private TokenServico tokenServico;

    // ENTRAR NO SISTEMA (Login)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AutenticacaoDTO dto) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());

        // Confere no banco se a senha bate. Se errar, dá erro 403
        var auth = this.authenticationManager.authenticate(usernamePassword);


        var token = tokenServico.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    //CADASTRAR NOVO USUÁRIO
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistroDTO dto) {
        // Se o e-mail já existe, dá erro 400
        if (this.repository.findByLogin(dto.login()) != null) return ResponseEntity.badRequest().build();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(dto.senha());

        Usuario novoUsuario = new Usuario(null, dto.login(), senhaCriptografada, dto.role(), null);
        this.repository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }
}
