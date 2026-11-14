package com.exercicio.msemprestimo.gateway;

import com.exercicio.msemprestimo.client.UsuarioClient;
import com.exercicio.msemprestimo.client.UsuarioClient.UsuarioResponse;
import feign.FeignException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioGateway {
    private final UsuarioClient usuarioClient;

    public UsuarioResponse buscar(UUID usuarioId) {
        try {
            return usuarioClient.buscar(usuarioId);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex);
        } catch (FeignException.FeignClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Resposta inesperada de msusuario", ex);
        } catch (FeignException ex) {
            log.error("Falha ao chamar msusuario: status {}", ex.status(), ex);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Falha na comunicação com msusuario", ex);
        }
    }
}
