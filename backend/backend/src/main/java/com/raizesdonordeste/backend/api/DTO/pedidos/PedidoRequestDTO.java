package com.raizesdonordeste.backend.api.DTO.pedidos;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record PedidoRequestDTO(

        @Schema(description = "lista de produtos e quantidades")
        List<ItemPedidoRequestDTO> itens,


        @Schema(example =  "totem", description =  "canal de origem (app, totem, balcão, pickup)")
        String canalPedido




) {
}
