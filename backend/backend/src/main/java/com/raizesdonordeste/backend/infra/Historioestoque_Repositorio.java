package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.estoque.movimentacoes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Historioestoque_Repositorio extends JpaRepository<movimentacoes, Long> {

    List<movimentacoes> findByUnidadeIdOrderByDataHoraDesc(Long unidadeId);


}
