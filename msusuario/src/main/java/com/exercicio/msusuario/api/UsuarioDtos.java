package com.exercicio.msusuario.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class UsuarioDtos {
    private UsuarioDtos() {}

    public record CriarUsuarioRequest(
            @NotBlank @Size(max = 150) String nome,
            @NotBlank @Email @Size(max = 180) String email,
            @NotBlank @Size(max = 40) String matricula) {}

    public record UsuarioResponse(UUID id, String nome, String email, String matricula) {}
}
