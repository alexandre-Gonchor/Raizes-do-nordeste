package com.raizesdonordeste.backend.infra;


import com.raizesdonordeste.backend.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface Usuario_Repositorio extends JpaRepository<Usuario, Long> {

    UserDetails findByLogin(String login);
}
