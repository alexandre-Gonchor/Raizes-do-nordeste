package com.raizesdonordeste.backend.configuracao;

import java.time.Instant;

public record ExecaoPadrao(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}
