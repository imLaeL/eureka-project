package com.exercicio.msusuario.repository;

import com.exercicio.msusuario.domain.Usuario;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    boolean existsByEmail(String email);
    boolean existsByMatricula(String matricula);
}
