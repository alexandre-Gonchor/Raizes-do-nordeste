package com.raizesdonordeste.backend.api.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequestDTO(

        @NotEmpty(message = "O pedido deve ter ao menos um item")
        @Schema(description = "lista de produtos e quantidades")
        List<ItemPedidoRequestDTO> itens,

        @NotBlank(message = "Canal do pedido é obrigatório (APP, WEB, TOTEM, BALCAO, PICKUP)")
        @Schema(example = "TOTEM", description = "canal de origem (APP, WEB, TOTEM, BALCAO, PICKUP)")
        String canalPedido,

        @NotNull(message = "ID do cliente é obrigatório")
        @Schema(example = "1", description = "ID do cliente para pontuação/resgate")
        Long ClienteId,

        @Schema(example = "false", description = "true para usar saldo de pontos como desconto")
        Boolean usarPontos,

        @Schema(example = "SAOJOAO20", description = "Código promocional para desconto (opcional)")
        String cupom
) {
}
