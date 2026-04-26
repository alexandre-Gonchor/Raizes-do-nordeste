package com.raizesdonordeste.backend.aplicacao;

import com.raizesdonordeste.backend.infra.Usuario_Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutorizacaoServico implements UserDetailsService {

    @Autowired
    private Usuario_Repositorio repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }
}