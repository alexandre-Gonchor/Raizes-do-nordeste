package com.raizesdonordeste.backend.dominio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name = "itens_pedido")
@Data
public class itemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //relação numero de itens em 1 pedido
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference
    private Pedidos pedido;

    //relação de produto para pedidos
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private  Integer quantidade;
}
