package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.Enums.Canal_Pedidos;
import com.raizesdonordeste.backend.dominio.pedidos.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Pedido_Repositorio extends JpaRepository< Pedidos, Long> {

    List<Pedidos> findByUnidadeIdAndDataCriacaoBetween(Long unidadeId, java.time.LocalDateTime inicio, java.time.LocalDateTime fim);

    List<Pedidos> findByCanalPedido(Canal_Pedidos canalPedido);
}
