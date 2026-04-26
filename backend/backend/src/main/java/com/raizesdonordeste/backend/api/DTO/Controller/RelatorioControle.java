package com.raizesdonordeste.backend.api.DTO.Controller;


import com.raizesdonordeste.backend.api.DTO.Response.HistoricoEstoqueDTO;
import com.raizesdonordeste.backend.api.DTO.Response.RelatorioDesempenhoDTO;
import com.raizesdonordeste.backend.dominio.pedidos.Pedidos;
import com.raizesdonordeste.backend.infra.Historioestoque_Repositorio;
import com.raizesdonordeste.backend.infra.Pedido_Repositorio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/Relatorios")
@SecurityRequirement(name = "bearerAuth")
public class RelatorioControle {

    @Autowired
    private Historioestoque_Repositorio historicoRepo;

    @Autowired
    private Pedido_Repositorio pedidoRepo;

    //Historio estoque
    @GetMapping("/estoque/{unidadeId}")
    public ResponseEntity<List<HistoricoEstoqueDTO>> relatorioEstoque(@PathVariable Long unidadeId){

        List<HistoricoEstoqueDTO> relatorio = historicoRepo.findByUnidadeIdOrderByDataHoraDesc(unidadeId)

                .stream()
                .map(historico -> new HistoricoEstoqueDTO(
                        historico.getDataHora(),
                        historico.getProduto().getNome(),
                        historico.getTipo(),
                        historico.getQuantidade(),
                        historico.getUsuario().getLogin() // quem fez a movimentação
                )).toList();

        return ResponseEntity.ok(relatorio);
    }

    //Desenpenho mensal
    @GetMapping("/vendas/{unidadeID}")
    public ResponseEntity<RelatorioDesempenhoDTO> relatorioVendas(
            @PathVariable Long unidadeID,
            @RequestParam int ano,
            @RequestParam int mes){

        //pega o primeiro dia e o ultimo minuto do mes escolhido
        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        List<Pedidos> pedidosMes = pedidoRepo.findByUnidadeIdAndDataCriacaoBetween( unidadeID, inicioMes, fimMes);

        int totalPedidos = pedidosMes.size();
        BigDecimal faturamentoTotal = pedidosMes.stream()
                .filter( p -> !p.getStatus().equalsIgnoreCase("CANCELADO"))
                .map(Pedidos::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String mesFormatado = inicioMes.format(DateTimeFormatter.ofPattern("MM/yyyy"));

        return ResponseEntity.ok(new RelatorioDesempenhoDTO(mesFormatado, totalPedidos, faturamentoTotal));



    }
}
