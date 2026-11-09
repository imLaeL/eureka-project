package com.exercicio.msemprestimo.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msequipamentos", path = "/api/v1/equipamentos")
public interface EquipamentoClient {
    @PutMapping("/{id}/reservas/{emprestimoId}")
    void reservar(@PathVariable UUID id, @PathVariable UUID emprestimoId);
    @DeleteMapping("/{id}/reservas/{emprestimoId}")
    void liberar(@PathVariable UUID id, @PathVariable UUID emprestimoId);
}
