package com.raizesdonordeste.backend.dominio;


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

    @Column (unique = true)
    private  Integer pontosAcumulados = 0;

    private String cpf;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private ClientePerfil perfil = ClientePerfil.OURO;
}
