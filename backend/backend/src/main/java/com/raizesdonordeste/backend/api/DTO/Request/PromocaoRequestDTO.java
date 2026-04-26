package com.raizesdonordeste.backend.api.DTO.Request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromocaoRequestDTO(

        String codigo,
        String descricao,
        BigDecimal percentualDesconto,
        LocalDate dataValidade
) {}
