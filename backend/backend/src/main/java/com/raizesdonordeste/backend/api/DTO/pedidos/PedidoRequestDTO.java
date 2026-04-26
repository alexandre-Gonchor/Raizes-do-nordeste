package com.raizesdonordeste.backend.api.DTO.pedidos;

import java.util.List;

import com.raizesdonordeste.backend.dominio.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;

public record PedidoRequestDTO(

        @Schema(description = "lista de produtos e quantidades")
        List<ItemPedidoRequestDTO> itens,


        @Schema(example =  "TOTEM", description =  "canal de origem (APP, TOTEM, BALCAO, PICKUP)")
        String canalPedido,


      @Schema(example = "1", description = "ID do cliente para pontuação/resgate")
                Long ClienteId,

        @Schema(example = "false", description = "Define se o cliente quer usar o saldo de pontos para desconto")
        Boolean usarPontos

) {
}
