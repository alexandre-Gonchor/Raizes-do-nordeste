package com.raizesdonordeste.backend.api.DTO.Response;

import java.math.BigDecimal;

public record ProdutoCatalogoDTO(

        Long produtoId,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidadeDisponivel
) {}
