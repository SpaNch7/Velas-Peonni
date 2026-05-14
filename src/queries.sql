-- =====================================================
-- QUERIES - Sistema Peonni
-- =====================================================

-- =====================================================
-- INNER JOIN - Produtos com suas categorias
-- =====================================================
SELECT p.nome AS produto, p.preco, p.estoque, c.nome AS categoria
FROM produto p
INNER JOIN categoria c ON p.categoria_id = c.id;

-- =====================================================
-- LEFT JOIN - Todos os produtos, mesmo sem categoria
-- =====================================================
SELECT p.nome AS produto, p.preco, c.nome AS categoria
FROM produto p
LEFT JOIN categoria c ON p.categoria_id = c.id;

-- =====================================================
-- RIGHT JOIN - Todas as categorias, mesmo sem produto
-- =====================================================
SELECT p.nome AS produto, c.nome AS categoria
FROM produto p
RIGHT JOIN categoria c ON p.categoria_id = c.id;

-- =====================================================
-- FULL OUTER JOIN - Produtos e categorias sem ligação
-- =====================================================
SELECT p.nome AS produto, c.nome AS categoria
FROM produto p
FULL OUTER JOIN categoria c ON p.categoria_id = c.id;

-- =====================================================
-- INNER JOIN multiplo - Pedido com cliente e itens
-- =====================================================
SELECT pe.id AS pedido_id, cl.nome AS cliente, pe.data,
       pr.nome AS produto, ip.quantidade
FROM pedido pe
INNER JOIN cliente cl ON pe.cliente_id = cl.id
INNER JOIN item_pedido ip ON ip.pedido_id = pe.id
INNER JOIN produto pr ON ip.produto_id = pr.id;

-- =====================================================
-- LEFT JOIN - Clientes que nunca fizeram pedido
-- =====================================================
SELECT cl.nome AS cliente, cl.email, pe.id AS pedido_id
FROM cliente cl
LEFT JOIN pedido pe ON pe.cliente_id = cl.id
WHERE pe.id IS NULL;

-- =====================================================
-- INNER JOIN com agregacao - Total de pedidos por cliente
-- =====================================================
SELECT cl.nome AS cliente, COUNT(pe.id) AS total_pedidos
FROM cliente cl
INNER JOIN pedido pe ON pe.cliente_id = cl.id
GROUP BY cl.nome
ORDER BY total_pedidos DESC;

-- =====================================================
-- INNER JOIN - Produtos mais vendidos
-- =====================================================
SELECT pr.nome AS produto, SUM(ip.quantidade) AS total_vendido
FROM produto pr
INNER JOIN item_pedido ip ON ip.produto_id = pr.id
GROUP BY pr.nome
ORDER BY total_vendido DESC;
