package com.raizesdonordeste.backend.api;

import com.raizesdonordeste.backend.dominio.Pedidos;
import com.raizesdonordeste.backend.servico.PedidoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.raizesdonordeste.backend.api.DTO.PedidoRequestDTO;

@RestController
@RequestMapping ("/pedidos")
public class pedidoControle {

    @Autowired
    private PedidoServico pedidoServico;

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
}
