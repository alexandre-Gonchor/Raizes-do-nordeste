package com.raizesdonordeste.backend.api.DTO.Response;
import com.raizesdonordeste.backend.dominio.Enums.Cargos;
public record RegistroDTO(String login, String senha, Cargos role) {}