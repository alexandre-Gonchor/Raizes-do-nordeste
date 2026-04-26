package com.raizesdonordeste.backend.api.DTO.Unidade;

import com.raizesdonordeste.backend.dominio.Unidade;
import com.raizesdonordeste.backend.infra.Unidade_Repositorio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
@SecurityRequirement(name = "bearerAuth")
public class UnidadeControle {

    @Autowired
    private Unidade_Repositorio unidadeRepo;

    // Rota para cadastrar uma nova unidade (Ex: Matriz)
    @PostMapping
    public ResponseEntity<Unidade> cadastrarUnidade(@RequestBody Unidade novaUnidade) {
        Unidade salva = unidadeRepo.save(novaUnidade);
        return ResponseEntity.status(201).body(salva);
    }

    // Rota para listar todas as unidades
    @GetMapping
    public ResponseEntity<List<Unidade>> listarUnidades() {
        return ResponseEntity.ok(unidadeRepo.findAll());
    }
}