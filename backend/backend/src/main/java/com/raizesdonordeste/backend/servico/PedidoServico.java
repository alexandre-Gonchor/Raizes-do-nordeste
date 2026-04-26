package com.raizesdonordeste.backend.servico;
import com.raizesdonordeste.backend.api.DTO.Request.ItemPedidoRequestDTO;
import com.raizesdonordeste.backend.api.DTO.Request.PedidoRequestDTO;
import com.raizesdonordeste.backend.dominio.Cliente;
import com.raizesdonordeste.backend.dominio.Enums.Canal_Pedidos;
import com.raizesdonordeste.backend.dominio.Enums.TipoMovimentacao;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.dominio.pedidos.Pedidos;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import com.raizesdonordeste.backend.dominio.pedidos.itemPedido;
import com.raizesdonordeste.backend.infra.Cliente_Repositorio;
import com.raizesdonordeste.backend.infra.Pedido_Repositorio;
import com.raizesdonordeste.backend.infra.Produto_Repositorio;
import com.raizesdonordeste.backend.infra.Usuario_Repositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class PedidoServico {

    @Autowired
    private Pedido_Repositorio pedidoRepo;

    @Autowired
    private Produto_Repositorio produtoRepo;

    @Autowired
    private Cliente_Repositorio clienteRepo;

    @Autowired
    private ClienteServico clienteServico;

    @Autowired
    private EstoqueServico estoqueServico;

    @Autowired
    private Usuario_Repositorio usuarioRepo;

    @Transactional
    public Pedidos criarNovoPedido(PedidoRequestDTO dto) {

        // 1. BUSCAR FUNCIONÁRIO LOGADO E SUA UNIDADE
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Erro de segurança: Usuário não está autenticado no sistema!");
        }
        String loginFuncionario = authentication.getName();



        var userDetails = usuarioRepo.findByLogin(loginFuncionario);
        if (userDetails == null) {
            throw new RuntimeException("Funcionário logado não encontrado no banco de dados!");
        }

        Usuario funcionario = (Usuario) userDetails;


        if (funcionario.getUnidade() == null) {
            throw new RuntimeException("Erro: Este funcionário não está vinculado a nenhuma unidade/estoque.");
        }

        Pedidos novoPedido = new Pedidos();
        novoPedido.setDataCriacao(LocalDateTime.now());
        novoPedido.setStatus("Pagamento_em_Aguardo");
        novoPedido.setCanalPedido(Canal_Pedidos.valueOf(dto.canalPedido()));

        // ASSOCIA O PEDIDO À UNIDADE DO FUNCIONÁRIO
        novoPedido.setUnidade(funcionario.getUnidade());

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

                // 2. BAIXA AUTOMÁTICA DE ESTOQUE

                estoqueServico.registrarMovimentacao(
                        produtoReal,
                        funcionario.getUnidade(),
                        funcionario,
                        item.getQuantidade(),
                        TipoMovimentacao.SAIDA
                );
            }
        }

        Cliente cliente = clienteRepo.findById(dto.ClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        novoPedido.setCliente(cliente);


        if (dto.usarPontos() != null && dto.usarPontos()) {
            int saldoPontos = cliente.getPontosAcumulados() != null ? cliente.getPontosAcumulados() : 0;
            if (saldoPontos > 0) {
                BigDecimal taxaConversao = new BigDecimal("10");
                BigDecimal desconto = new BigDecimal(saldoPontos).divide(taxaConversao, 2, RoundingMode.HALF_UP);

                if (desconto.compareTo(totalPedido) > 0) {
                    desconto = totalPedido;
                }
                totalPedido = totalPedido.subtract(desconto);
                cliente.setPontosAcumulados(0);
                clienteRepo.save(cliente);
            }
        }

        novoPedido.setItens(itensReais);
        novoPedido.setValorTotal(totalPedido);

        Pedidos pedidoSalvo = pedidoRepo.save(novoPedido);
        clienteServico.adicionarPontos(cliente.getId(), totalPedido);

        return pedidoSalvo;
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
