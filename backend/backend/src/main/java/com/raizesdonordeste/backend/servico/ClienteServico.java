package com.raizesdonordeste.backend.servico;

import com.raizesdonordeste.backend.api.DTO.cliente.ClienterequestDTO;
import com.raizesdonordeste.backend.dominio.Cliente;
import com.raizesdonordeste.backend.dominio.Enums.ClientePerfil;
import com.raizesdonordeste.backend.infra.Cliente_repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // <--- Importação necessária!
import java.time.LocalDate;

@Service
public class ClienteServico {

    @Autowired
    private Cliente_repositorio repositorio;

    public Cliente salvar(ClienterequestDTO dto) {
        Cliente cliente = new Cliente();

        // Mapeamento de dados
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setCpf(dto.cpf());
        cliente.setDataNascimento(dto.dataNascimento());

        // LGPD: Tratamento de consentimento
        cliente.setMarketing(dto.termosAceitos());
        if (dto.termosAceitos()) {
            cliente.setDataConsentimento(LocalDate.now());
        }

        // Regras de Fidelização Iniciais
        cliente.setPerfil(ClientePerfil.OURO);
        cliente.setPontosAcumulados(0);

        return repositorio.save(cliente);
    }

    @Transactional

    public void adicionarPontos(Long clienteID, BigDecimal valorPedido){

        Cliente cliente = repositorio.findById(clienteID)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        int pontosGanhos = valorPedido.intValue();

        int saldoAtual = cliente.getPontosAcumulados() != null ? cliente.getPontosAcumulados() : 0;
        cliente.setPontosAcumulados(saldoAtual + pontosGanhos);

        repositorio.save(cliente);
    }
}