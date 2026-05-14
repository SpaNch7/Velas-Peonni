CREATE OR REPLACE FUNCTION verificar_estoque()
RETURNS TRIGGER AS $$
DECLARE
    estoque_atual INT;
BEGIN
    SELECT estoque INTO estoque_atual
    FROM produto
    WHERE id = NEW.produto_id;

    IF estoque_atual < NEW.quantidade THEN
        RAISE EXCEPTION 'Estoque insuficiente!';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_verificar_estoque
BEFORE INSERT ON item_pedido
FOR EACH ROW
EXECUTE FUNCTION verificar_estoque();

CREATE OR REPLACE FUNCTION diminuir_estoque()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE produto
    SET estoque = estoque - NEW.quantidade
    WHERE id = NEW.produto_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_diminuir_estoque
AFTER INSERT ON item_pedido
FOR EACH ROW
EXECUTE FUNCTION diminuir_estoque();

CREATE TABLE log_produto (
    id SERIAL PRIMARY KEY,
    produto_id INT,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION logar_produto()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO log_produto (produto_id)
    VALUES (NEW.id);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_log_produto
AFTER INSERT ON produto
FOR EACH ROW
EXECUTE FUNCTION logar_produto();
