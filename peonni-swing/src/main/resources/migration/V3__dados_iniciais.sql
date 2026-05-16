-- =====================================================
-- V3 - Dados iniciais do sistema Peonni
-- =====================================================

INSERT INTO categoria (nome) VALUES ('Relaxante');
INSERT INTO categoria (nome) VALUES ('Aromatica');
INSERT INTO categoria (nome) VALUES ('Decorativa');

INSERT INTO cliente (nome, email) VALUES ('Ana Lima', 'ana@email.com');
INSERT INTO cliente (nome, email) VALUES ('Bruno Costa', 'bruno@email.com');
INSERT INTO cliente (nome, email) VALUES ('Carla Souza', 'carla@email.com');

INSERT INTO produto (nome, preco, estoque, categoria_id) VALUES ('Vela Lavanda', 35.90, 50, 1);
INSERT INTO produto (nome, preco, estoque, categoria_id) VALUES ('Vela Baunilha', 29.90, 30, 2);
INSERT INTO produto (nome, preco, estoque, categoria_id) VALUES ('Vela Madeira', 45.00, 20, 3);
INSERT INTO produto (nome, preco, estoque, categoria_id) VALUES ('Vela Rosa', 39.90, 40, 1);
INSERT INTO produto (nome, preco, estoque, categoria_id) VALUES ('Vela Canela', 32.50, 25, 2);
