package com.raizesdonordeste.backend.dominio.auditoria;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

// LGPD Art. 37 + rastreabilidade: persiste ações sensíveis sem armazenar dados pessoais além do necessário
@Entity
@Table(name = "auditoria")
@Data
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ação realizada (ex: PEDIDO_CRIADO, STATUS_ALTERADO, LOGIN)
    @Column(nullable = false, length = 50)
    private String acao;

    // Login do usuário responsável — identificação mínima necessária
    @Column(nullable = false, length = 100)
    private String usuario;

    // Contexto adicional (id do recurso, valores de/para, etc.)
    @Column(length = 500)
    private String detalhe;

    @Column(nullable = false)
    private LocalDateTime dataHora;
}