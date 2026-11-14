package com.exercicio.msequipamento.api;

import com.exercicio.msequipamento.api.EquipamentoDtos.*;
import com.exercicio.msequipamento.service.EquipamentoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {
    private final EquipamentoService service;

    @PostMapping
    public ResponseEntity<EquipamentoResponse> criar(@Valid @RequestBody CriarEquipamentoRequest request) {
        EquipamentoResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/api/v1/equipamentos/" + response.id())).body(response);
    }
    @GetMapping("/{id}") public EquipamentoResponse buscar(@PathVariable UUID id) { return service.buscar(id); }
    @GetMapping("/disponiveis") public List<EquipamentoResponse> disponiveis() { return service.disponiveis(); }
    @PutMapping("/{id}/reservas/{emprestimoId}")
    public ResponseEntity<Void> reservar(@PathVariable UUID id, @PathVariable UUID emprestimoId) {
        service.reservar(id, emprestimoId); return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}/reservas/{emprestimoId}")
    public ResponseEntity<Void> liberar(@PathVariable UUID id, @PathVariable UUID emprestimoId) {
        service.liberar(id, emprestimoId); return ResponseEntity.noContent().build();
    }
}
