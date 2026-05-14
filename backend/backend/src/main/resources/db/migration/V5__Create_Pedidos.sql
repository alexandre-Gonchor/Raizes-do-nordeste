CREATE TABLE IF NOT EXISTS pedidos (
    id BIGSERIAL PRIMARY KEY,
    canal_pedido VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    valor_total NUMERIC(19, 2),
    data_criacao TIMESTAMP,
    cliente_id BIGINT REFERENCES cliente(id),
    unidade_id BIGINT REFERENCES unidade(id)
);

CREATE TABLE IF NOT EXISTS itens_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedidos(id),
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    quantidade INTEGER NOT NULL
);
