package com.raizesdonordeste.backend.api.DTO.Relatorio;

import java.math.BigDecimal;

public record RelatorioDesenpenhoDTO(

        String mesAno,
        Integer totalPedidos,
        BigDecimal FaturamentoTotal
) {
}
