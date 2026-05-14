package com.raizesdonordeste.backend.dominio;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizesdonordeste.backend.dominio.Enums.ClientePerfil;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Cliente {

    //lgpd de marketing
    private  Boolean marketing = false;
    private LocalDate dataConsentimento;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column
    private Integer pontosAcumulados = 0;

    // LGPD: dado sensível — aceito no cadastro mas nunca retornado em responses
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cpf;

    // LGPD: dado pessoal — não exposto; usado apenas internamente para perfil de cliente
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private ClientePerfil perfil = ClientePerfil.OURO;
}
