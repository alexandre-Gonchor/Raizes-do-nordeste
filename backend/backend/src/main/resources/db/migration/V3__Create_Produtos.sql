CREATE TABLE IF NOT EXISTS produtos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(500),
    preco NUMERIC(19, 2) NOT NULL,
    disponivel BOOLEAN NOT NULL
);
