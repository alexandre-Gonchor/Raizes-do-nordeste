package com.raizesdonordeste.backend.api.DTO.Controller;


import com.raizesdonordeste.backend.api.DTO.Request.PromocaoRequestDTO;
import com.raizesdonordeste.backend.dominio.pedidos.Promocao;
import com.raizesdonordeste.backend.infra.Promocao_Repositorio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promocoes")
@SecurityRequirement(name = "bearerAuth")
public class PromocaoControle {

    @Autowired
    private Promocao_Repositorio repositorio;

    @PostMapping
    public ResponseEntity<String> criarPromocao(@RequestBody PromocaoRequestDTO dto) {
        Promocao novaPromocao = new Promocao(dto.codigo(), dto.descricao(), dto.percentualDesconto(), dto.dataValidade());
        repositorio.save(novaPromocao);
        return ResponseEntity.status(201).body("Promoção " + dto.codigo() + " criada com sucesso!");
    }
}
