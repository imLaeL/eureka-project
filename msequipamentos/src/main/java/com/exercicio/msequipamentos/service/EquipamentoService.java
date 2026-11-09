package com.exercicio.msequipamentos.service;

import com.exercicio.msequipamentos.api.EquipamentoDtos.*;
import com.exercicio.msequipamentos.domain.Equipamento;
import com.exercicio.msequipamentos.repository.EquipamentoRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EquipamentoService {
    private final EquipamentoRepository repository;

    @Transactional
    public EquipamentoResponse criar(CriarEquipamentoRequest request) {
        if (repository.existsByPatrimonio(request.patrimonio())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Patrimônio já cadastrado");
        }
        try {
            return toResponse(repository.save(Equipamento.builder().id(UUID.randomUUID())
                    .nome(request.nome()).tipo(request.tipo()).patrimonio(request.patrimonio()).disponivel(true).build()));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Patrimônio já cadastrado", ex);
        }
    }

    @Transactional(readOnly = true)
    public EquipamentoResponse buscar(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<EquipamentoResponse> disponiveis() {
        return repository.findByDisponivelTrue().stream().map(this::toResponse).toList();
    }

    @Transactional
    public void reservar(UUID id, UUID emprestimoId) {
        Equipamento equipamento = repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));
        if (emprestimoId.equals(equipamento.getReservaEmprestimoId())) return;
        if (!equipamento.isDisponivel()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipamento indisponível");
        }
        equipamento.reservar(emprestimoId);
    }

    @Transactional
    public void liberar(UUID id, UUID emprestimoId) {
        Equipamento equipamento = repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));
        if (emprestimoId.equals(equipamento.getReservaEmprestimoId())) equipamento.liberar();
    }

    private EquipamentoResponse toResponse(Equipamento e) {
        return new EquipamentoResponse(e.getId(), e.getNome(), e.getTipo(), e.getPatrimonio(), e.isDisponivel());
    }
}
