package com.raizesdonordeste.backend.api.DTO.Request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record PedidoRequestDTO(

        @Schema(description = "lista de produtos e quantidades")
        List<ItemPedidoRequestDTO> itens,


        @Schema(example =  "TOTEM", description =  "canal de origem (APP, TOTEM, BALCAO, PICKUP)")
        String canalPedido,


      @Schema(example = "1", description = "ID do cliente para pontuação/resgate")
                Long ClienteId,

        @Schema(example = "false", description = "Define se o cliente quer usar o saldo de pontos para desconto")
        Boolean usarPontos,

        @Schema(example = "SAOJOAO20", description = "Código promocional para desconto (opcional)")
        String cupom



) {
}
