package com.raizesdonordeste.backend.api.DTO.Controller;


import com.raizesdonordeste.backend.api.DTO.Response.ProdutoCatalogoDTO;
import com.raizesdonordeste.backend.dominio.estoque.Estoque;
import com.raizesdonordeste.backend.infra.EstoqueUnidade_Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cardapio")

public class CardapioControle {

    @Autowired
    private EstoqueUnidade_Repositorio estoqueUnidadeRepo;

    @GetMapping("/vitrine/{unidadeId}")
    public ResponseEntity<List<ProdutoCatalogoDTO>> visualizarVitrine(@PathVariable Long unidadeId){

        List<Estoque> estoquesDisponiveis = estoqueUnidadeRepo.buscarVitrineDoTotem(unidadeId);

        List<ProdutoCatalogoDTO> vitrine = estoquesDisponiveis.stream()
                .map(estoque -> new ProdutoCatalogoDTO(
                        estoque.getProduto().getId(),
                        estoque.getProduto().getNome(),
                        estoque.getProduto().getDescricao(),
                        estoque.getProduto().getPreco(),
                        estoque.getQuantidadeAtual()
                ) ).toList();

        return ResponseEntity.ok(vitrine);
    }
}
