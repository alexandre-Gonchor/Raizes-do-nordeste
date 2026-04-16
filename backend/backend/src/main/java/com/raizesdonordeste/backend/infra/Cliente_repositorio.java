package com.raizesdonordeste.backend.infra;

import com.raizesdonordeste.backend.dominio.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Cliente_repositorio extends JpaRepository<Cliente, Long> {

    Iterable<Cliente> findBymarketingTrue();

    Optional<Cliente> findBycpf(String cpf);

    Optional<Cliente> findByemail(String email);
}
