package com.raizesdonordeste.backend.api.DTO.cliente;

import com.raizesdonordeste.backend.dominio.Cliente;
import com.raizesdonordeste.backend.servico.ClienteServico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes") // Define que o endereço será http://localhost:8080/clientes
@SecurityRequirement(name = "bearerAuth")
public class ClienteControle {

    @Autowired
    private ClienteServico servico;

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody ClienterequestDTO dto) {

        Cliente novoCliente = servico.salvar(dto);


        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }
}
