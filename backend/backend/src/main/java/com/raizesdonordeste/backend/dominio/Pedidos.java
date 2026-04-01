package com.raizesdonordeste.backend.dominio;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos" )
@Data

public class Pedidos {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private long id;

    //arquivar ID do cliente
    @Column(name = "canal_pedido", nullable=false)
    private String canalPedido;

    //aguardo de pagamentos, em preparo, pronto
    @Column (nullable = false)
    private String status;

    @Column(name = "Valor_total")
    private BigDecimal valorTotal;

    @Column(name = "data_Criacao")
    private LocalDateTime dataCriacao;

    //relação de itens no pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL )
    private List <itemPedido> itens;

}
