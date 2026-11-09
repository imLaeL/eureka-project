package com.exercicio.msemprestimo.api;

import com.exercicio.msemprestimo.domain.StatusEmprestimo;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public final class EmprestimoDtos {
    private EmprestimoDtos() {}
    public record CriarEmprestimoRequest(@NotNull UUID usuarioId, @NotNull UUID equipamentoId) {}
    public record EmprestimoResponse(UUID id, UUID usuarioId, UUID equipamentoId, LocalDateTime dataEmprestimo,
                                     LocalDateTime dataDevolucao, StatusEmprestimo status) {}
}
