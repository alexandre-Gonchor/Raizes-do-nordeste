package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.itemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface Item_repositorio  extends JpaRepository<itemPedido, Long> {

}
