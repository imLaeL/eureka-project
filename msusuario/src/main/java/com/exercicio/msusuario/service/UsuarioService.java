package com.exercicio.msusuario.service;

import com.exercicio.msusuario.api.UsuarioDtos.CriarUsuarioRequest;
import com.exercicio.msusuario.api.UsuarioDtos.UsuarioResponse;
import com.exercicio.msusuario.domain.Usuario;
import com.exercicio.msusuario.repository.UsuarioRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioResponse criar(CriarUsuarioRequest request) {
        if (repository.existsByEmail(request.email()) || repository.existsByMatricula(request.matricula())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail ou matrícula já cadastrados");
        }
        try {
            Usuario usuario = repository.save(Usuario.builder()
                    .id(UUID.randomUUID()).nome(request.nome()).email(request.email()).matricula(request.matricula()).build());
            return toResponse(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail ou matrícula já cadastrados", ex);
        }
    }

    public UsuarioResponse buscar(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getMatricula());
    }
}
