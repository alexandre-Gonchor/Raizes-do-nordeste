package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.estoque.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstoqueUnidade_Repositorio extends JpaRepository<Estoque, Long> {

    @Query("SELECT e FROM Estoque e WHERE e.unidade.id = :unidadeId AND e.quantidadeAtual > 0")
    List<Estoque> buscarVitrineDoTotem(@Param("unidadeId") Long unidadeId);

}
