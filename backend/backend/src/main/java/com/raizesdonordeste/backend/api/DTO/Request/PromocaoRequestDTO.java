package com.raizesdonordeste.backend.api.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromocaoRequestDTO(

        @NotBlank(message = "Código da promoção é obrigatório")
        @Pattern(regexp = "^[A-Z0-9]{3,20}$", message = "Código deve ter 3-20 caracteres alfanuméricos maiúsculos")
        @Schema(example = "SAOJOAO20", description = "Código do cupom (maiúsculas, sem espaços)")
        String codigo,

        @NotBlank(message = "Descrição é obrigatória")
        @Schema(example = "Desconto de São João 2025")
        String descricao,

        @NotNull(message = "Percentual de desconto é obrigatório")
        @DecimalMin(value = "0.1", message = "Desconto mínimo de 0.1%")
        @DecimalMax(value = "100.0", message = "Desconto máximo de 100%")
        @Schema(example = "15.00", description = "Percentual de desconto (0.1 a 100)")
        BigDecimal percentualDesconto,

        @NotNull(message = "Data de validade é obrigatória")
        @Future(message = "Data de validade deve ser no futuro")
        @Schema(example = "2025-07-01", description = "Data de expiração do cupom")
        LocalDate dataValidade
) {}
