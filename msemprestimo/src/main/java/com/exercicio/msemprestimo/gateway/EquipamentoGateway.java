package com.exercicio.msemprestimo.gateway;

import com.exercicio.msemprestimo.api.AtualizarCondicaoRequest;
import com.exercicio.msemprestimo.client.EquipamentoClient;
import com.exercicio.msemprestimo.client.EquipamentoClient.EquipamentoResponse;
import com.exercicio.msemprestimo.enums.CondicaoEnum;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipamentoGateway {
    private final EquipamentoClient equipamentoClient;

    @CircuitBreaker(name = "atualizarCondicao", fallbackMethod = "fallback")
    public EquipamentoResponse atualizarCondicao(UUID equipamentoId, CondicaoEnum condicao) {
        return equipamentoClient.atualizarCondicao(equipamentoId, new AtualizarCondicaoRequest(condicao));
    }

    private EquipamentoResponse fallback(UUID equipamentoId, CondicaoEnum condicao, Throwable ex) {
        if (ex instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado", ex);
        }
        if (ex instanceof FeignException.FeignClientException) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Resposta inesperada de msequipamento", ex);
        }
        log.error("Falha ao atualizar condição do equipamento {}", equipamentoId, ex);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "msequipamento indisponível", ex);
    }
}
