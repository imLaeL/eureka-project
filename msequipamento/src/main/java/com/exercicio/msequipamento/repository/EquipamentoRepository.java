package com.exercicio.msequipamento.repository;

import com.exercicio.msequipamento.domain.Equipamento;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;

public interface EquipamentoRepository extends JpaRepository<Equipamento, UUID> {
    List<Equipamento> findByDisponivelTrue();
    boolean existsByPatrimonio(String patrimonio);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Equipamento e where e.id = :id")
    java.util.Optional<Equipamento> findByIdForUpdate(UUID id);
}
