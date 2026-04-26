package com.raizesdonordeste.backend.aplicacao;

import com.raizesdonordeste.backend.dominio.Enums.TipoMovimentacao;
import com.raizesdonordeste.backend.dominio.Unidade;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.dominio.estoque.Estoque;
import com.raizesdonordeste.backend.dominio.estoque.movimentacoes;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import com.raizesdonordeste.backend.infra.Estoque_Repositorio;
import com.raizesdonordeste.backend.infra.Historioestoque_Repositorio;
import com.raizesdonordeste.backend.infra.Usuario_Repositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EstoqueServico {

    @Autowired
    private Estoque_Repositorio estoqueRepo;

    @Autowired
    private Historioestoque_Repositorio historicoRepo;


    @Autowired
    private Usuario_Repositorio usuarioRepo;

    @Transactional
    public void registrarMovimentacao(Produto produto, Unidade unidade, Usuario usuario, Integer quantidade, TipoMovimentacao tipo) {

        Estoque estoqueAtual = estoqueRepo.findByProdutoAndUnidade(produto, unidade)
                .orElse(new Estoque());

        if (estoqueAtual.getId() ==null){
            estoqueAtual.setProduto(produto);
            estoqueAtual.setUnidade(unidade);
            estoqueAtual.setQuantidadeAtual(0);
        }

        if (tipo == TipoMovimentacao.ENTRADA) {
            estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() + quantidade);
        } else if (tipo == TipoMovimentacao.SAIDA) {
            if (estoqueAtual.getQuantidadeAtual() < quantidade) {
                throw new RuntimeException("Estoque insuficiente para esta saída");
            }
            estoqueAtual.setQuantidadeAtual(estoqueAtual.getQuantidadeAtual() - quantidade);
        }

        estoqueRepo.save(estoqueAtual);


        movimentacoes historico = new movimentacoes();
        historico.setTipo(tipo);
        historico.setQuantidade(quantidade);
        historico.setDataHora(LocalDateTime.now());
        historico.setProduto(produto);
        historico.setUnidade(unidade);
        historico.setUsuario(usuario);

        historicoRepo.save(historico);
    }


    }

