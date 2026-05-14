package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.auditoria.RegistroAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Auditoria_Repositorio extends JpaRepository<RegistroAuditoria, Long> {
}