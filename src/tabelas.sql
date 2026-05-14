-- =====================================================
-- Script de criação das tabelas - Peonni
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

CREATE TABLE log_produto (
    id SERIAL PRIMARY KEY,
    produto_id INT,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_produto FOREIGN KEY (produto_id) REFERENCES produto(id)
);

-- =====================================================
-- CRUD - categoria
-- =====================================================
INSERT INTO categoria (nome) VALUES ('Relaxante');
UPDATE categoria SET nome = 'Relaxante Premium' WHERE id = 1;
SELECT * FROM categoria;
DELETE FROM categoria WHERE id = 1;

-- =====================================================
-- CRUD - cliente
-- =====================================================
INSERT INTO cliente (nome, email) VALUES ('Ana Lima', 'ana@email.com');
UPDATE cliente SET email = 'ana.lima@email.com' WHERE id = 1;
SELECT * FROM cliente;
DELETE FROM cliente WHERE id = 1;

-- =====================================================
-- CRUD - produto
-- =====================================================
INSERT INTO produto (nome, preco, estoque, categoria_id) VALUES ('Vela Lavanda', 35.90, 50, 1);
UPDATE produto SET preco = 39.90 WHERE id = 1;
SELECT * FROM produto;
DELETE FROM produto WHERE id = 1;

-- =====================================================
-- CRUD - pedido
-- =====================================================
INSERT INTO pedido (cliente_id, data) VALUES (1, CURRENT_DATE);
UPDATE pedido SET data = CURRENT_DATE WHERE id = 1;
SELECT * FROM pedido;
DELETE FROM pedido WHERE id = 1;

-- =====================================================
-- CRUD - item_pedido
-- =====================================================
INSERT INTO item_pedido (pedido_id, produto_id, quantidade) VALUES (1, 1, 2);
UPDATE item_pedido SET quantidade = 3 WHERE id = 1;
SELECT * FROM item_pedido;
DELETE FROM item_pedido WHERE id = 1;
