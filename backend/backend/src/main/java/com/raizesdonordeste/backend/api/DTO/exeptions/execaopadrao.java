package com.raizesdonordeste.backend.api.DTO.exeptions;

import java.time.Instant;

public record execaopadrao(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}
