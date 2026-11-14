package com.exercicio.msequipamento.domain;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "tb_equipamentos")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Equipamento {
    @Id
    @Column(columnDefinition = "char(36)")
    private UUID id;
    @Column(nullable = false, length = 150) private String nome;
    @Column(nullable = false, length = 60) private String tipo;
    @Column(nullable = false, unique = true, length = 60) private String patrimonio;
    @Column(nullable = false) private boolean disponivel;
    @Column(columnDefinition = "char(36)") private UUID reservaEmprestimoId;

    public void reservar(UUID emprestimoId) { this.disponivel = false; this.reservaEmprestimoId = emprestimoId; }
    public void liberar() { this.disponivel = true; this.reservaEmprestimoId = null; }
}
