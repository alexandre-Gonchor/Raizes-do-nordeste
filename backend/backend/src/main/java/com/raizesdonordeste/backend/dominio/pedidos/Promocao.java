package com.raizesdonordeste.backend.dominio.pedidos;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "promocoes")
@Entity(name = "Promocao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Promocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String codigo;

    private String descricao;

    private BigDecimal percentualDesconto;

    private LocalDate dataValidade;

    private Boolean ativa;


    public Promocao(String codigo, String descricao, BigDecimal percentualDesconto, LocalDate dataValidade){
        this.codigo = codigo.toUpperCase();
        this.descricao = descricao;
        this.percentualDesconto = percentualDesconto;
        this.dataValidade = dataValidade;
        this.ativa = true;
    }
    public void desativar() {
        this.ativa = false;
    }



}
