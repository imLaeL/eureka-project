package com.exercicio.msequipamentos.api;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public final class EquipamentoDtos {
    private EquipamentoDtos() {}
    public record CriarEquipamentoRequest(@NotBlank String nome, @NotBlank String tipo, @NotBlank String patrimonio) {}
    public record EquipamentoResponse(UUID id, String nome, String tipo, String patrimonio, boolean disponivel) {}
}
