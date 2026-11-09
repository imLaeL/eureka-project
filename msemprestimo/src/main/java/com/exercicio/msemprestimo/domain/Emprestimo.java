package com.exercicio.msemprestimo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "tb_emprestimos")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {
    @Id
    @Column(columnDefinition = "char(36)") private UUID id;
    @Column(nullable = false, columnDefinition = "char(36)") private UUID usuarioId;
    @Column(nullable = false, columnDefinition = "char(36)") private UUID equipamentoId;
    @Column(nullable = false) private LocalDateTime dataEmprestimo;
    private LocalDateTime dataDevolucao;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private StatusEmprestimo status;

    public void devolver() {
        if (status == StatusEmprestimo.ATIVO) {
            status = StatusEmprestimo.DEVOLVIDO;
            dataDevolucao = LocalDateTime.now();
        }
    }
}
