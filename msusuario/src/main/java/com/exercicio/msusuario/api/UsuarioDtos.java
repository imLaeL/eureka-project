package com.exercicio.msusuario.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public final class UsuarioDtos {
    private UsuarioDtos() {}

    public record CriarUsuarioRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String matricula) {}

    public record UsuarioResponse(UUID id, String nome, String email, String matricula) {}
}
