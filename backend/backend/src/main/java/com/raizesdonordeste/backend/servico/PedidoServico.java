package com.raizesdonordeste.backend.servico;
import com.raizesdonordeste.backend.api.DTO.pedidos.ItemPedidoRequestDTO;
import com.raizesdonordeste.backend.api.DTO.pedidos.PedidoRequestDTO;
import com.raizesdonordeste.backend.dominio.Enums.Canal_Pedidos;
import com.raizesdonordeste.backend.dominio.pedidos.Pedidos;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import com.raizesdonordeste.backend.dominio.pedidos.itemPedido;
import com.raizesdonordeste.backend.infra.Pedido_Repositorio;
import com.raizesdonordeste.backend.infra.Produto_repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class PedidoServico {

    @Autowired
    private Pedido_Repositorio pedidoRepo;

    @Autowired
    private Produto_repositorio produtoRepo;


    //public de criação dos pedidos
    public Pedidos criarNovoPedido(PedidoRequestDTO dto) { // Alterado para receber o DTO

        // cria a instancia da entidade
        Pedidos novoPedido = new Pedidos();

        // definidos dados do DTO
        novoPedido.setDataCriacao(LocalDateTime.now());
        novoPedido.setStatus("Pagamento_em_Aguardo");
        novoPedido.setCanalPedido(Canal_Pedidos.valueOf(dto.canalPedido()));

        BigDecimal totalPedido = BigDecimal.ZERO;
        List<itemPedido> itensReais = new ArrayList<>();


        if (dto.itens() != null) {
            for (ItemPedidoRequestDTO itemDto : dto.itens()) {


                Produto produtoReal = produtoRepo.findById(itemDto.produtoID())
                        .orElseThrow(() -> new RuntimeException("produto não encontrado"));


                itemPedido item = new itemPedido();
                item.setProduto(produtoReal);
                item.setPedido(novoPedido);
                item.setQuantidade(itemDto.quantidade());


                BigDecimal quantidade = new BigDecimal(item.getQuantidade());
                BigDecimal subtotalItem = produtoReal.getPreco().multiply(quantidade);

                totalPedido = totalPedido.add(subtotalItem);
                itensReais.add(item);
            }
        }

        novoPedido.setItens(itensReais);
        novoPedido.setValorTotal(totalPedido);

        return pedidoRepo.save(novoPedido);
    }



    //public de atualização do status do pedido
    public Pedidos atualizarStatus(Long id, String atualizado) {
        Pedidos pedidos = pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // impede a mudança em pedidos ja finalizados
        if (pedidos.getStatus().equals("ENTREGUE") || pedidos.getStatus().equals("CANCELADO")) {
            throw new IllegalStateException("alteração não é possivel em um pedido ja finalizado");
        }

        pedidos.setStatus(atualizado);

        System.out.println("log: Pedido" + id + "alterado para" + atualizado + "em" + java.time.LocalDateTime.now());

        return pedidoRepo.save(pedidos);
    }



    //public que simula o sistema de pagamento com 10% de chance de falha
    public Pedidos processarPagamento (Long id){
        Pedidos pedidos = pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        boolean aprovado = Math.random() > 0.1;

        if (aprovado) {
            pedidos.setStatus("Pago");

        }else{
            pedidos.setStatus("pagamento recusado");
        }
        return pedidoRepo.save(pedidos);
    }

}
