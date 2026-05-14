package com.raizesdonordeste.backend.configuracao;

import com.raizesdonordeste.backend.dominio.Enums.Cargos;
import com.raizesdonordeste.backend.dominio.Usuario;
import com.raizesdonordeste.backend.infra.Usuario_Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeedConfig implements ApplicationRunner {

    @Autowired
    private Usuario_Repositorio usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepo.findByLogin("admin") != null) return;

        Usuario admin = new Usuario(
                null,
                "admin",
                passwordEncoder.encode("admin123"),
                Cargos.ADMIN,
                null
        );
        usuarioRepo.save(admin);
    }
}
