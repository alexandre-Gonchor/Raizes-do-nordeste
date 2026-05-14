package com.raizesdonordeste.backend.configuracao;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class resourceExceptionHandler {

    // 404 — recurso não encontrado (produto, pedido, cliente, unidade, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExecaoPadrao> entityNotFound(RuntimeException e, HttpServletRequest request) {
        ExecaoPadrao err = new ExecaoPadrao(
                Instant.now(), 404, "Recurso não encontrado", e.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    // 422 — regra de negócio violada (ex.: alterar pedido já finalizado, estoque insuficiente)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExecaoPadrao> unprocessable(IllegalStateException e, HttpServletRequest request) {
        ExecaoPadrao err = new ExecaoPadrao(
                Instant.now(), 422, "Operação não permitida", e.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    // 400 — parâmetro inválido passado pelo cliente
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExecaoPadrao> badArgument(IllegalArgumentException e, HttpServletRequest request) {
        ExecaoPadrao err = new ExecaoPadrao(
                Instant.now(), 400, "Parâmetro inválido", e.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // 409 — violação de unicidade no banco (e-mail, CPF, CNPJ, código de cupom duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExecaoPadrao> conflict(DataIntegrityViolationException e, HttpServletRequest request) {
        ExecaoPadrao err = new ExecaoPadrao(
                Instant.now(), 409, "Conflito de dados",
                "Este registro (e-mail, CPF, CNPJ ou código) já está cadastrado no sistema.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    // 400 — falha de sintaxe JSON
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ExecaoPadrao> badJson(
            org.springframework.http.converter.HttpMessageNotReadableException e, HttpServletRequest request) {
        ExecaoPadrao err = new ExecaoPadrao(
                Instant.now(), 400, "Erro na sintaxe do JSON",
                "Verifique se o corpo da requisição está no formato correto (chaves, vírgulas e nomes dos campos).",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // 400 — campos obrigatórios ausentes ou com formato inválido (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExecaoPadrao> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        // Coleta todos os erros de campo para mensagem clara
        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> "'" + fe.getField() + "': " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ExecaoPadrao err = new ExecaoPadrao(
                Instant.now(), 400, "Erro de validação", mensagem, request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
