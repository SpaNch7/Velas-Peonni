SELECT p.nome AS produto, c.nome AS categoria
FROM produto p
JOIN categoria c ON p.categoria_id = c.id;