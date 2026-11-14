package com.exercicio.msequipamento.domain;

import com.exercicio.msequipamento.enums.CondicaoEnum;
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
    @Setter
    @Column(nullable = false) private boolean disponivel;
    @Setter
    @Column(columnDefinition = "char(36)") private UUID reservaEmprestimoId;

    @Setter
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CondicaoEnum condicao = CondicaoEnum.BOM;
}
