package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.pedidos.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Pedido_Repositorio extends JpaRepository< Pedidos, Long> {
}
