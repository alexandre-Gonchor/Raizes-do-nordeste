package com.raizesdonordeste.backend.api.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UnidadeRequestDTO(

        @NotBlank(message = "nome é obrigatório")
        @Schema(example = "Raízes do Nordeste - Recife Centro", description = "nome da unidade")
        String nome,

        @NotBlank(message = "cnpj é obrigatório")
        @Schema(example = "12.345.678/0001-90", description = "cnpj da unidade")
        String cnpj,

        @NotBlank(message = "endereço é obrigatório")
        @Schema(example = "Av. Boa Viagem, 1000 - Recife/PE", description = "endereço completo")
        String endereco
) {}
