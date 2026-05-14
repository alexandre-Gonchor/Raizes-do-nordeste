package com.raizesdonordeste.backend.api.DTO.Controller;

import com.raizesdonordeste.backend.api.DTO.Request.PromocaoRequestDTO;
import com.raizesdonordeste.backend.dominio.pedidos.Promocao;
import com.raizesdonordeste.backend.infra.Promocao_Repositorio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promocoes")
@SecurityRequirement(name = "bearerAuth")
public class PromocaoControle {

    @Autowired
    private Promocao_Repositorio repositorio;

    @Operation(summary = "Criar cupom de desconto", description = "Cria um cupom promocional. Restrito a administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Promoção criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (código, percentual ou data)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer ROLE_ADMIN"),
            @ApiResponse(responseCode = "409", description = "Código de cupom já cadastrado")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> criarPromocao(@Valid @RequestBody PromocaoRequestDTO dto) {
        Promocao novaPromocao = new Promocao(dto.codigo(), dto.descricao(), dto.percentualDesconto(), dto.dataValidade());
        repositorio.save(novaPromocao);
        return ResponseEntity.status(201).body("Promoção " + dto.codigo() + " criada com sucesso!");
    }
}
