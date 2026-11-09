package com.exercicio.msemprestimo.service;

import com.exercicio.msemprestimo.api.EmprestimoDtos.*;
import com.exercicio.msemprestimo.client.*;
import com.exercicio.msemprestimo.domain.*;
import com.exercicio.msemprestimo.repository.EmprestimoRepository;
import feign.FeignException;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final EmprestimoRepository repository;
    private final UsuarioClient usuarioClient;
    private final EquipamentoClient equipamentoClient;

    @Transactional
    public EmprestimoResponse criar(CriarEmprestimoRequest request) {
        validarUsuario(request.usuarioId());
        UUID id = UUID.randomUUID();
        try {
            equipamentoClient.reservar(request.equipamentoId(), id);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado", ex);
        } catch (FeignException.Conflict ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipamento indisponível", ex);
        } catch (FeignException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Falha na comunicação com msequipamentos", ex);
        }
        Emprestimo emprestimo = repository.save(Emprestimo.builder().id(id).usuarioId(request.usuarioId())
                .equipamentoId(request.equipamentoId()).dataEmprestimo(LocalDateTime.now())
                .status(StatusEmprestimo.ATIVO).build());
        return toResponse(emprestimo);
    }

    @Transactional(readOnly = true)
    public EmprestimoResponse buscar(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> porUsuario(UUID usuarioId) {
        return repository.findByUsuarioIdOrderByDataEmprestimoDesc(usuarioId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public EmprestimoResponse devolver(UUID id) {
        Emprestimo emprestimo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO) return toResponse(emprestimo);
        try {
            equipamentoClient.liberar(emprestimo.getEquipamentoId(), id);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado", ex);
        } catch (FeignException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Falha na comunicação com msequipamentos", ex);
        }
        emprestimo.devolver();
        return toResponse(emprestimo);
    }

    private void validarUsuario(UUID id) {
        try {
            usuarioClient.buscar(id);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex);
        } catch (FeignException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Falha na comunicação com msusuario", ex);
        }
    }

    private EmprestimoResponse toResponse(Emprestimo e) {
        return new EmprestimoResponse(e.getId(), e.getUsuarioId(), e.getEquipamentoId(), e.getDataEmprestimo(),
                e.getDataDevolucao(), e.getStatus());
    }
}
