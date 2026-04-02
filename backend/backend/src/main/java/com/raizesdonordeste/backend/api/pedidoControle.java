package com.raizesdonordeste.backend.api;

import com.raizesdonordeste.backend.dominio.Pedidos;
import com.raizesdonordeste.backend.servico.PedidoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/pedidos")
public class pedidoControle {

    @Autowired
    private PedidoServico pedidoServico;

    @PostMapping
    public ResponseEntity<Pedidos> criarPedido(@RequestBody Pedidos pedidos) {

        Pedidos novoPedido = pedidoServico.criarNovoPedido(pedidos);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }
}
