package com.exercicio.msemprestimo.repository;

import com.exercicio.msemprestimo.domain.Emprestimo;
import com.exercicio.msemprestimo.domain.StatusEmprestimo;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {
    List<Emprestimo> findByUsuarioIdOrderByDataEmprestimoDesc(UUID usuarioId);
    boolean existsByEquipamentoIdAndStatus(UUID equipamentoId, StatusEmprestimo status);
}
