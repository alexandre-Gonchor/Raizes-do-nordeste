package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.Unidade;
import com.raizesdonordeste.backend.dominio.estoque.Estoque;
import com.raizesdonordeste.backend.dominio.pedidos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Estoque_Repositorio extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByProdutoAndUnidade(Produto produto, Unidade unidade);
}
