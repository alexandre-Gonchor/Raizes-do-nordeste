package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface Pedido_Repositorio extends JpaRepository< Pedidos, Long> {
}
