package com.raizesdonordeste.backend.api.DTO.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;




public record ClienterequestDTO(

        @NotBlank(message = "o email é obrigatorio")
        @Schema (example = "formato invalido")
        String email,

        @NotBlank(message = "o nome é obrigatorio")
        String nome,

        @NotBlank(message = "cpf é obrigatorio")
        @Schema (example = "400.289.225-00")
        String cpf,

        @NotNull(message = "data de nascimento é obtigatorio")
        LocalDate dataNascimento,

        @Schema(description = "os termos foram aceitos pelo cliente?", example = "true")
        Boolean termosAceitos,

        @Schema(description =  "versão dos termos", example = "v9.0")
        String termosVersao


) {
}
