package com.raizesdonordeste.backend.api.DTO.Controller;

import com.raizesdonordeste.backend.dominio.Enums.TipoMovimentacao;
import com.raizesdonordeste.backend.dominio.Unidade;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import com.raizesdonordeste.backend.infra.Produto_Repositorio;
import com.raizesdonordeste.backend.infra.Unidade_Repositorio;
import com.raizesdonordeste.backend.infra.Usuario_Repositorio;
import com.raizesdonordeste.backend.aplicacao.EstoqueServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estoque")
@SecurityRequirement(name = "bearerAuth")
public class EstoqueControle {

    @Autowired
    private EstoqueServico estoqueServico;

    @Autowired
    private Produto_Repositorio produtoRepo;

    @Autowired
    private Unidade_Repositorio unidadeRepo;

    @Autowired
    private Usuario_Repositorio usuarioRepo;

    public record MovimentacaoDTO(
            @NotNull(message = "ID do produto é obrigatório") Long produtoId,
            @NotNull(message = "ID da unidade é obrigatório") Long unidadeId,
            @NotNull @Min(value = 1, message = "Quantidade mínima é 1") Integer quantidade,
            @NotNull(message = "Tipo de movimentação é obrigatório (ENTRADA ou SAIDA)") TipoMovimentacao tipo
    ) {}

    @Operation(summary = "Registrar movimentação de estoque", description = "Entrada ou saída manual de produtos em uma unidade. Registra histórico com usuário responsável.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimentação registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto ou unidade não encontrado"),
            @ApiResponse(responseCode = "422", description = "Estoque insuficiente para saída solicitada")
    })
    @PostMapping("/movimentacao")
    public ResponseEntity<String> registrarMovimentacao(@Valid @RequestBody MovimentacaoDTO dto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        Usuario gerente = (Usuario) usuarioRepo.findByLogin(authentication.getName());
        if (gerente == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Produto produto = produtoRepo.findById(dto.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Unidade unidade = unidadeRepo.findById(dto.unidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

        estoqueServico.registrarMovimentacao(produto, unidade, gerente, dto.quantidade(), dto.tipo());

        return ResponseEntity.ok("Movimentação de " + dto.tipo() + " registrada com sucesso!");
    }
}
