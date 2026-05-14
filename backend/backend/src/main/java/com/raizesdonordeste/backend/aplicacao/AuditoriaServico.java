package com.raizesdonordeste.backend.aplicacao;

import com.raizesdonordeste.backend.dominio.auditoria.RegistroAuditoria;
import com.raizesdonordeste.backend.infra.Auditoria_Repositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditoriaServico {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaServico.class);

    @Autowired
    private Auditoria_Repositorio auditoriaRepo;

    // Persiste no banco E emite log estruturado para rastreabilidade completa
    public void registrar(String acao, String usuario, String detalhe) {
        RegistroAuditoria registro = new RegistroAuditoria();
        registro.setAcao(acao);
        registro.setUsuario(usuario);
        registro.setDetalhe(detalhe);
        registro.setDataHora(LocalDateTime.now());

        auditoriaRepo.save(registro);

        log.info("[AUDITORIA] acao={} usuario={} detalhe={}", acao, usuario, detalhe);
    }
}