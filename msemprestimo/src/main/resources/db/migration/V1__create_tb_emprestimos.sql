CREATE TABLE tb_emprestimos (
    id CHAR(36) NOT NULL,
    usuario_id CHAR(36) NOT NULL,
    equipamento_id CHAR(36) NOT NULL,
    data_emprestimo DATETIME NOT NULL,
    data_devolucao DATETIME NULL,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_emprestimos_usuario (usuario_id),
    INDEX idx_emprestimos_equipamento_status (equipamento_id, status)
);
