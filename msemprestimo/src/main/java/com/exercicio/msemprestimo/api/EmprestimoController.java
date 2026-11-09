package com.exercicio.msemprestimo.api;

import com.exercicio.msemprestimo.api.EmprestimoDtos.*;
import com.exercicio.msemprestimo.service.EmprestimoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {
    private final EmprestimoService service;
    @PostMapping
    public ResponseEntity<EmprestimoResponse> criar(@Valid @RequestBody CriarEmprestimoRequest request) {
        EmprestimoResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/api/v1/emprestimos/" + response.id())).body(response);
    }
    @GetMapping("/{id}") public EmprestimoResponse buscar(@PathVariable UUID id) { return service.buscar(id); }
    @GetMapping("/usuarios/{usuarioId}") public List<EmprestimoResponse> porUsuario(@PathVariable UUID usuarioId) { return service.porUsuario(usuarioId); }
    @PatchMapping("/{id}/devolucao") public EmprestimoResponse devolver(@PathVariable UUID id) { return service.devolver(id); }
}
