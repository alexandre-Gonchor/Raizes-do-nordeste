package com.raizesdonordeste.backend.servico;

import com.raizesdonordeste.backend.dominio.Pedidos;
import com.raizesdonordeste.backend.dominio.Produto;
import com.raizesdonordeste.backend.dominio.itemPedido;
import com.raizesdonordeste.backend.infra.Pedido_Repositorio;
import com.raizesdonordeste.backend.infra.Produto_repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
public class PedidoServico {

    @Autowired
    private Pedido_Repositorio pedidoRepo;

    @Autowired
    private Produto_repositorio produtoRepo;

    public Pedidos criarNovoPedido(Pedidos pedidos){

        //criação e definição de data automatica
        pedidos.setDataCriacao(LocalDateTime.now());

        //todos os pedidos começam em "pagamento em aguardo"
        pedidos.setStatus("Pagamento_em_Aguardo");

        BigDecimal totalPedido = BigDecimal.ZERO;

        //calcular o valor total inicial como zero
        BigDecimal total = BigDecimal.ZERO;
        if ( pedidos.getItens() != null){
            for (itemPedido item : pedidos.getItens()){

                //faz a busca do produto no banco de dados utilziando o ID
                Produto produtoReal = produtoRepo.findById(item.getProduto().getId())
                        .orElseThrow(() -> new RuntimeException("produto não encontrado"));

                //atualiza o item com o produto real
                item.setProduto(produtoReal);
                item.setPedido(pedidos); //linka o item a capa do pedido

                //Calcula o subtotal do pedido (Preço real * quantidade)
                BigDecimal quantidade = new BigDecimal(item.getQuantidade());
                BigDecimal subtotalItem = produtoReal.getPreco().multiply(quantidade);

                totalPedido = totalPedido.add(subtotalItem);
            }
        }

        //valor total dos itens e preço
        pedidos.setValorTotal(totalPedido);

        //salva o pedido no bancod e dados utilizando o repositorio
        return pedidoRepo.save(pedidos);
    }
}
