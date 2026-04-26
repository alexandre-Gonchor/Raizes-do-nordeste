package com.raizesdonordeste.backend.infra;


import com.raizesdonordeste.backend.dominio.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Unidade_Repositorio extends JpaRepository<Unidade, Long> {
}
