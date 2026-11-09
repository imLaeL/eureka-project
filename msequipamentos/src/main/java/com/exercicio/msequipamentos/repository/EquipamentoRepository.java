package com.exercicio.msequipamentos.repository;

import com.exercicio.msequipamentos.domain.Equipamento;
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
