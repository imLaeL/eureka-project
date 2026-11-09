package com.exercicio.msemprestimo.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msusuario", path = "/api/v1/usuarios")
public interface UsuarioClient {
    @GetMapping("/{id}") UsuarioResponse buscar(@PathVariable UUID id);
    record UsuarioResponse(UUID id, String nome, String email, String matricula) {}
}
