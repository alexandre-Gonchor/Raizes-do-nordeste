package com.raizesdonordeste.backend.api.DTO.Response;

import java.math.BigDecimal;

public record RelatorioDesempenhoDTO(

        String mesAno,
        Integer totalPedidos,
        BigDecimal FaturamentoTotal
) {
}
