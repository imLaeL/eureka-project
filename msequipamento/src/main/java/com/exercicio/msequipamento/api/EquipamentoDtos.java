package com.exercicio.msequipamento.api;

import com.exercicio.msequipamento.enums.CondicaoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class EquipamentoDtos {
    private EquipamentoDtos() {}

    public record CriarEquipamentoRequest(
            @NotBlank @Size(max = 150) String nome,
            @NotBlank @Size(max = 60) String tipo,
            @NotBlank @Size(max = 60) String patrimonio) {}

    public record AtualizarCondicaoRequest(@NotNull CondicaoEnum condicao) {}

    public record EquipamentoResponse(UUID id, String nome, String tipo, String patrimonio, boolean disponivel,
                                      CondicaoEnum condicao) {}
}
