package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produto_Repositorio extends JpaRepository<Produto, Long> {
}
