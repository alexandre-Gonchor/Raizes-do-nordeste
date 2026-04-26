package com.raizesdonordeste.backend.api.DTO.Controller;

import com.raizesdonordeste.backend.api.DTO.Request.PedidoRequestDTO;
import com.raizesdonordeste.backend.dominio.Enums.Canal_Pedidos;
import com.raizesdonordeste.backend.dominio.pedidos.Pedidos;
import com.raizesdonordeste.backend.infra.Pedido_Repositorio;
import com.raizesdonordeste.backend.aplicacao.PedidoServico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping ("/pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoControle {

    @Autowired
    private PedidoServico pedidoServico;

    @Autowired
    private Pedido_Repositorio pedidoRepo;

    @PostMapping
    public ResponseEntity<Pedidos> criarPedido(@RequestBody PedidoRequestDTO dto) {

        Pedidos novoPedido = pedidoServico.criarNovoPedido(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    @Operation(summary = "pagar pedido", description = "Simula opagamento e atualiza o status do pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento processado (Aprovado ou Recusado)"),
            @ApiResponse(responseCode = "404", description = "ID do Pedido não encontrado no banco de dados")
    })
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Pedidos> pagarPedido(@PathVariable Long id) {
        Pedidos pedidoAtualizado = pedidoServico.processarPagamento(id);
        return ResponseEntity.ok(pedidoAtualizado);
    }



    public record AtualizarStatusDTO(String status) {}

    @PutMapping("/{id}/status")
    public ResponseEntity<String> atualizarStatusPedido(@PathVariable Long id, @RequestBody AtualizarStatusDTO dto) {

        pedidoServico.atualizarStatus(id, dto.status());
        return ResponseEntity.ok("Status do pedido " + id + " atualizado para: " + dto.status());
    }


    @GetMapping
    public ResponseEntity<List<Pedidos>> consultarPedidos(@RequestParam(required = false) Canal_Pedidos canal) {
        if (canal != null) {
            return ResponseEntity.ok(pedidoRepo.findByCanalPedido(canal));
        }
        return ResponseEntity.ok(pedidoRepo.findAll());
    }
    }

