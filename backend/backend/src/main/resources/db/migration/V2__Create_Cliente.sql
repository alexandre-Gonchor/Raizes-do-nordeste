CREATE TABLE IF NOT EXISTS cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255),
    cpf VARCHAR(255),
    data_nascimento DATE,
    pontos_acumulados INTEGER UNIQUE DEFAULT 0,
    perfil VARCHAR(50) DEFAULT 'OURO',
    marketing BOOLEAN DEFAULT FALSE,
    data_consentimento DATE
);
