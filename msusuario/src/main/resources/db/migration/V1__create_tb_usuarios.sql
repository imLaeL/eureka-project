CREATE TABLE tb_usuarios (
    id CHAR(36) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(180) NOT NULL,
    matricula VARCHAR(40) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT uk_usuarios_matricula UNIQUE (matricula)
);
