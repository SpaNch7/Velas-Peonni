-- =====================================================
-- V1 - Criação das tabelas do sistema Peonni
-- =====================================================

CREATE TABLE categoria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL DEFAULT 0,
    categoria_id INT,
    CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE pedido (
    id SERIAL PRIMARY KEY,
    cliente_id INT,
    data DATE NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE item_pedido (
    id SERIAL PRIMARY KEY,
    pedido_id INT,
    produto_id INT,
    quantidade INT NOT NULL,
    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produto(id)
);

-- Tabela auxiliar usada pelos triggers
CREATE TABLE log_produto (
    id SERIAL PRIMARY KEY,
    produto_id INT,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
