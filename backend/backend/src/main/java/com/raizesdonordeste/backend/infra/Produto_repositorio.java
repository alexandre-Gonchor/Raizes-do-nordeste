package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface Produto_repositorio extends JpaRepository<Produto, Long> {
}
