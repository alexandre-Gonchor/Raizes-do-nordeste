package com.raizesdonordeste.backend.configuracao;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class resourceExceptionHandler {

    // erros de busca 404
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExecaoPadrao> entityNotFound(RuntimeException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExecaoPadrao err = new ExecaoPadrao(Instant.now(), status.value(), "Recurso não encontrado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // erro de sintaxe 400
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ExecaoPadrao> badRequest(org.springframework.http.converter.HttpMessageNotReadableException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400
        ExecaoPadrao err = new ExecaoPadrao(
                Instant.now(),
                status.value(),
                "Erro na sintaxe do JSON",
                "Verifique se o corpo da requisição está no formato correto (chaves, vírgulas e nomes dos campos).",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    //mostra exatamente o campo que o cliente não preencheu corretamente
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ExecaoPadrao> validation(org.springframework.web.bind.MethodArgumentNotValidException e, HttpServletRequest request) {


        String mensagemErro = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        HttpStatus status = HttpStatus.BAD_REQUEST; // Código 400

        ExecaoPadrao err = new ExecaoPadrao(
                java.time.Instant.now(),
                status.value(),
                "Erro de integridade de dados",
                "Este registro (E-mail, CPF ou Nome) já está cadastrado no sistema.",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }
}
