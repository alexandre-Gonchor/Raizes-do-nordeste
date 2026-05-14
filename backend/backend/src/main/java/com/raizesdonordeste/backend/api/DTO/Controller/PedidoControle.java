package com.raizesdonordeste.backend.api.DTO.Controller;

import com.raizesdonordeste.backend.api.DTO.Request.PedidoRequestDTO;
import com.raizesdonordeste.backend.dominio.Enums.Canal_Pedidos;
import com.raizesdonordeste.backend.dominio.Enums.StatusPedido;
import com.raizesdonordeste.backend.dominio.pedidos.Pedidos;
import com.raizesdonordeste.backend.infra.Pedido_Repositorio;
import com.raizesdonordeste.backend.aplicacao.PedidoServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoControle {

    @Autowired
    private PedidoServico pedidoServico;

    @Autowired
    private Pedido_Repositorio pedidoRepo;

    @Operation(summary = "Criar pedido", description = "Abre um novo pedido associado ao canal e funcionário logado. Baixa estoque automaticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com status AGUARDANDO_PAGAMENTO"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou itens ausentes"),
            @ApiResponse(responseCode = "404", description = "Cliente, produto ou unidade não encontrado"),
            @ApiResponse(responseCode = "422", description = "Estoque insuficiente para um dos itens")
    })
    @PostMapping
    public ResponseEntity<Pedidos> criarPedido(@Valid @RequestBody PedidoRequestDTO dto) {
        Pedidos novoPedido = pedidoServico.criarNovoPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    @Operation(summary = "Processar pagamento", description = "Simula pagamento com 90% de aprovação. Atualiza status para PAGO ou PAGAMENTO_RECUSADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento processado (PAGO ou PAGAMENTO_RECUSADO)"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Pedidos> pagarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoServico.processarPagamento(id));
    }

    @Operation(summary = "Atualizar status do pedido", description = "Avança o status do pedido. Restrito a administradores. Não permite alterar pedidos ENTREGUE ou CANCELADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer ROLE_ADMIN"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pedido já finalizado, alteração não permitida")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<String> atualizarStatusPedido(@PathVariable Long id, @RequestBody AtualizarStatusDTO dto) {
        pedidoServico.atualizarStatus(id, dto.status());
        return ResponseEntity.ok("Status do pedido " + id + " atualizado para: " + dto.status());
    }

    @Operation(summary = "Listar pedidos", description = "Retorna todos os pedidos ou filtra por canal. Restrito a administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer ROLE_ADMIN")
    })
    @GetMapping
    public ResponseEntity<List<Pedidos>> consultarPedidos(@RequestParam(required = false) Canal_Pedidos canal) {
        if (canal != null) {
            return ResponseEntity.ok(pedidoRepo.findByCanalPedido(canal));
        }
        return ResponseEntity.ok(pedidoRepo.findAll());
    }

    public record AtualizarStatusDTO(StatusPedido status) {}
}
