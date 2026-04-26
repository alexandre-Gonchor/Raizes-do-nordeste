package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.pedidos.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Promocao_Repositorio extends JpaRepository<Promocao, Long> {


    Optional<Promocao> findByCodigoAndAtivaTrue(String codigo);
}