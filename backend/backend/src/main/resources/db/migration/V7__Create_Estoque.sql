CREATE TABLE IF NOT EXISTS estoque_unidade (
    id BIGSERIAL PRIMARY KEY,
    unidade_id BIGINT NOT NULL REFERENCES unidade(id),
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    quantidade_atual INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS historico_movimentacoes (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    quantidade INTEGER NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    unidade_id BIGINT NOT NULL REFERENCES unidade(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id)
);
