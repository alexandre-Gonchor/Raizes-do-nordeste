package com.raizesdonordeste.backend.aplicacao;
import com.raizesdonordeste.backend.api.DTO.Request.ItemPedidoRequestDTO;
import com.raizesdonordeste.backend.api.DTO.Request.PedidoRequestDTO;
import com.raizesdonordeste.backend.dominio.Cliente;
import com.raizesdonordeste.backend.dominio.Enums.Canal_Pedidos;
import com.raizesdonordeste.backend.dominio.Enums.StatusPedido;
import com.raizesdonordeste.backend.dominio.Enums.TipoMovimentacao;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.dominio.pedidos.Pedidos;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import com.raizesdonordeste.backend.dominio.pedidos.itemPedido;
import com.raizesdonordeste.backend.infra.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(PedidoServico.class);

    @Autowired
    private AuditoriaServico auditoriaServico;

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

    @Autowired
    private Promocao_Repositorio promocaoRepo;

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
        novoPedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
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


        //LÓGICA DA PROMOÇÃO (CUPOM)

        if (dto.cupom() != null && !dto.cupom().isBlank()) {
            var promocao = promocaoRepo.findByCodigoAndAtivaTrue(dto.cupom().toUpperCase());

            if (promocao.isPresent()) {
                BigDecimal porcentagem = promocao.get().getPercentualDesconto().divide(new BigDecimal("100"));
                BigDecimal valorDoDesconto = totalPedido.multiply(porcentagem);
                totalPedido = totalPedido.subtract(valorDoDesconto);
            } else {
                throw new RuntimeException("Cupom inválido ou expirado!");
            }
        }


        //LÓGICA DOS PONTOS DE FIDELIDADE
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

        auditoriaServico.registrar(
                "PEDIDO_CRIADO",
                loginFuncionario,
                "pedidoId=" + pedidoSalvo.getId() + " canal=" + pedidoSalvo.getCanalPedido() + " total=" + pedidoSalvo.getValorTotal()
        );

        return pedidoSalvo;
    }





    public Pedidos atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedidos pedidos = pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedidos.getStatus() == StatusPedido.ENTREGUE || pedidos.getStatus() == StatusPedido.CANCELADO) {
            throw new IllegalStateException("Alteração não é possível em pedido já finalizado");
        }

        StatusPedido statusAnterior = pedidos.getStatus();
        pedidos.setStatus(novoStatus);

        String usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        auditoriaServico.registrar(
                "STATUS_PEDIDO_ALTERADO",
                usuarioLogado,
                "pedidoId=" + id + " de=" + statusAnterior + " para=" + novoStatus
        );

        return pedidoRepo.save(pedidos);
    }



    // Simula pagamento com 10% de chance de falha
    public Pedidos processarPagamento(Long id) {
        Pedidos pedidos = pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        boolean aprovado = Math.random() > 0.1;
        StatusPedido novoStatus = aprovado ? StatusPedido.PAGO : StatusPedido.PAGAMENTO_RECUSADO;
        pedidos.setStatus(novoStatus);

        String usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        auditoriaServico.registrar(
                "PAGAMENTO_PROCESSADO",
                usuarioLogado,
                "pedidoId=" + id + " resultado=" + novoStatus
        );

        return pedidoRepo.save(pedidos);
    }

}
