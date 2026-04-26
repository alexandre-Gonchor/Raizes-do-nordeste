package com.raizesdonordeste.backend.api.DTO.Controller;

import com.raizesdonordeste.backend.api.DTO.Request.UnidadeRequestDTO;
import com.raizesdonordeste.backend.dominio.Unidade;
import com.raizesdonordeste.backend.infra.Unidade_Repositorio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<Unidade> cadastrarUnidade(@RequestBody @Valid UnidadeRequestDTO dto) {
        Unidade novaUnidade = new Unidade();
        novaUnidade.setNome(dto.nome());
        novaUnidade.setCnpj(dto.cnpj());
        novaUnidade.setEndereco(dto.endereco());
        Unidade salva = unidadeRepo.save(novaUnidade);
        return ResponseEntity.status(201).body(salva);
    }

    @GetMapping
    public ResponseEntity<List<Unidade>> listarUnidades() {
        return ResponseEntity.ok(unidadeRepo.findAll());
    }
}

