CREATE TABLE tb_equipamentos (
    id CHAR(36) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    tipo VARCHAR(60) NOT NULL,
    patrimonio VARCHAR(60) NOT NULL,
    disponivel BOOLEAN NOT NULL,
    reserva_emprestimo_id CHAR(36) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_equipamentos_patrimonio UNIQUE (patrimonio)
);
