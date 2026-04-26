package com.raizesdonordeste.backend.api.DTO.Unidade;

import com.raizesdonordeste.backend.dominio.Enums.TipoMovimentacao;

import java.time.LocalDateTime;

public record HistoricoEstoqueDTO(
        LocalDateTime dataHora,
        String produtoNome,
        TipoMovimentacao tipo,
        Integer quantidade,
        String responsavelNome
) {}
