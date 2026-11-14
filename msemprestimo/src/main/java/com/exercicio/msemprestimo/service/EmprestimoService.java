package com.exercicio.msemprestimo.service;

import com.exercicio.msemprestimo.api.EmprestimoDtos.*;
import com.exercicio.msemprestimo.client.*;
import com.exercicio.msemprestimo.domain.*;
import com.exercicio.msemprestimo.repository.EmprestimoRepository;
import feign.FeignException;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private static final String MSUSUARIO = "msusuario";
    private static final String MSEQUIPAMENTO = "msequipamento";

    private final EmprestimoRepository repository;
    private final UsuarioClient usuarioClient;
    private final EquipamentoClient equipamentoClient;

    public EmprestimoResponse criar(CriarEmprestimoRequest request) {
        validarUsuario(request.usuarioId());
        consultarEquipamento(request.equipamentoId());
        UUID id = UUID.randomUUID();
        reservarEquipamento(request.equipamentoId(), id);
        try {
            Emprestimo emprestimo = repository.save(Emprestimo.builder().id(id).usuarioId(request.usuarioId())
                    .equipamentoId(request.equipamentoId()).dataEmprestimo(LocalDateTime.now())
                    .status(StatusEmprestimo.ATIVO).build());
            return toResponse(emprestimo);
        } catch (RuntimeException ex) {
            compensarReserva(request.equipamentoId(), id);
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public EmprestimoResponse buscar(UUID id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> porUsuario(UUID usuarioId) {
        return repository.findByUsuarioIdOrderByDataEmprestimoDesc(usuarioId).stream().map(this::toResponse).toList();
    }

    public EmprestimoResponse devolver(UUID id) {
        Emprestimo emprestimo = buscarEntidade(id);
        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO) {
            return toResponse(emprestimo);
        }
        liberarEquipamento(emprestimo.getEquipamentoId(), id);
        emprestimo.devolver();
        return toResponse(repository.save(emprestimo));
    }

    private Emprestimo buscarEntidade(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
    }

    private void validarUsuario(UUID usuarioId) {
        try {
            usuarioClient.buscar(usuarioId);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex);
        } catch (FeignException ex) {
            throw falhaComunicacao(MSUSUARIO, ex);
        }
    }

    private void consultarEquipamento(UUID equipamentoId) {
        try {
            equipamentoClient.buscar(equipamentoId);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado", ex);
        } catch (FeignException ex) {
            throw falhaComunicacao(MSEQUIPAMENTO, ex);
        }
    }

    private void reservarEquipamento(UUID equipamentoId, UUID emprestimoId) {
        try {
            equipamentoClient.reservar(equipamentoId, emprestimoId);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado", ex);
        } catch (FeignException.Conflict ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipamento indisponível", ex);
        } catch (FeignException ex) {
            compensarReserva(equipamentoId, emprestimoId);
            throw falhaComunicacao(MSEQUIPAMENTO, ex);
        }
    }

    private void liberarEquipamento(UUID equipamentoId, UUID emprestimoId) {
        try {
            equipamentoClient.liberar(equipamentoId, emprestimoId);
        } catch (FeignException.NotFound ex) {
            log.warn("Equipamento {} não encontrado ao liberar empréstimo {}", equipamentoId, emprestimoId);
        } catch (FeignException ex) {
            throw falhaComunicacao(MSEQUIPAMENTO, ex);
        }
    }

    private void compensarReserva(UUID equipamentoId, UUID emprestimoId) {
        try {
            equipamentoClient.liberar(equipamentoId, emprestimoId);
        } catch (FeignException ex) {
            log.error("Falha ao compensar reserva do equipamento {} para empréstimo {}", equipamentoId, emprestimoId, ex);
        }
    }

    private ResponseStatusException falhaComunicacao(String servico, FeignException ex) {
        log.error("Falha ao chamar {}: status {}", servico, ex.status(), ex);
        boolean erroDoCliente = ex.status() >= 400 && ex.status() < 500;
        if (erroDoCliente) {
            return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Resposta inesperada de " + servico, ex);
        }
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Falha na comunicação com " + servico, ex);
    }

    private EmprestimoResponse toResponse(Emprestimo e) {
        return new EmprestimoResponse(e.getId(), e.getUsuarioId(), e.getEquipamentoId(), e.getDataEmprestimo(),
                e.getDataDevolucao(), e.getStatus());
    }
}
