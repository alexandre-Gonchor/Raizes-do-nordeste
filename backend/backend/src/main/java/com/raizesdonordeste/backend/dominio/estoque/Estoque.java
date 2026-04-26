package com.raizesdonordeste.backend.dominio.estoque;


import com.raizesdonordeste.backend.dominio.Unidade;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table ( name = "estoque_unidade")
@Data
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A qual unidade este estoque pertence?
    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    // Qual é o produto?
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private
    Produto produto;

    // Quantos tem na prateleira desta unidade?
    @Column(nullable = false)
    private Integer quantidadeAtual;
}
