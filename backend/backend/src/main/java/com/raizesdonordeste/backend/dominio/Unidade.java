package com.raizesdonordeste.backend.dominio;


import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "unidade")
@Data
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnpj;

    private String endereco;

    @OneToMany (mappedBy = "unidade")
    @JsonIgnore
    private List<Usuario> equipe;
}
