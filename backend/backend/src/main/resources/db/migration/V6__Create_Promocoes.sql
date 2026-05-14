CREATE TABLE IF NOT EXISTS promocoes (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(255),
    descricao VARCHAR(255),
    percentual_desconto NUMERIC(19, 2),
    data_validade DATE,
    ativa BOOLEAN
);
