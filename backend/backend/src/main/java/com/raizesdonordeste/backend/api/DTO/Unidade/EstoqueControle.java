package com.raizesdonordeste.backend.api.DTO.Unidade;

import com.raizesdonordeste.backend.dominio.Enums.TipoMovimentacao;
import com.raizesdonordeste.backend.dominio.Unidade;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import com.raizesdonordeste.backend.infra.Produto_repositorio;
import com.raizesdonordeste.backend.infra.Unidade_Repositorio;
import com.raizesdonordeste.backend.infra.Usuario_repositorio;
import com.raizesdonordeste.backend.servico.EstoqueServico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private Produto_repositorio produtoRepo;

    @Autowired
    private Unidade_Repositorio unidadeRepo;

    @Autowired
    private Usuario_repositorio usuarioRepo;


    public record MovimentacaoDTO(Long produtoId, Long unidadeId, Integer quantidade, TipoMovimentacao tipo) {}


    @PostMapping("/movimentacao")
    public ResponseEntity<String> registrarMovimentacao(@RequestBody MovimentacaoDTO dto) {


        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Erro: Usuário não autenticado no sistema!");
        }

        String login = authentication.getName();
        var userDetails = usuarioRepo.findByLogin(login);
        if (userDetails == null) {
            throw new RuntimeException("Usuário não encontrado no banco de dados!");
        }
        Usuario gerente = (Usuario) userDetails;


        Produto produto = produtoRepo.findById(dto.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
        Unidade unidade = unidadeRepo.findById(dto.unidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada!"));


        estoqueServico.registrarMovimentacao(produto, unidade, gerente, dto.quantidade(), dto.tipo());

        return ResponseEntity.ok("Movimentação de " + dto.tipo() + " registrada com sucesso!");
    }
}