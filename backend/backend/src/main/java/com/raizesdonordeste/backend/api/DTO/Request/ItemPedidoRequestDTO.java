package com.raizesdonordeste.backend.api.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItemPedidoRequestDTO(
        @Schema(example = "1", description = "ID do produto cadastrado")
        Long produtoID,


        @Schema(example = "2", description = "quantidade desejada")
        Integer quantidade



){
}
