package com.exercicio.msemprestimo.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msequipamento", path = "/api/v1/equipamentos")
public interface EquipamentoClient {
    @GetMapping("/{id}") EquipamentoResponse buscar(@PathVariable UUID id);
    @PutMapping("/{id}/reservas/{emprestimoId}")
    void reservar(@PathVariable UUID id, @PathVariable UUID emprestimoId);
    @DeleteMapping("/{id}/reservas/{emprestimoId}")
    void liberar(@PathVariable UUID id, @PathVariable UUID emprestimoId);
    record EquipamentoResponse(UUID id, String nome, String tipo, String patrimonio, boolean disponivel) {}
}
