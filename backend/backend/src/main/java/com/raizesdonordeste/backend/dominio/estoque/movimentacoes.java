package com.raizesdonordeste.backend.dominio.estoque;

import com.raizesdonordeste.backend.dominio.Enums.TipoMovimentacao;
import com.raizesdonordeste.backend.dominio.Unidade;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_movimentacoes")
@Data
public class movimentacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo; // ENTRADA ou SAIDA

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private LocalDateTime dataHora;


    //qual produto foi movimentado
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;


    //em qual unidade
    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    //qual usuario fez a movimentação
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
