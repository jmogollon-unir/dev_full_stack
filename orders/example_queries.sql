-- ========================================
-- 1. CREAR NUEVO PEDIDO
-- ========================================
INSERT INTO orders (user_id, address, city, country, phone, status_id, total)
VALUES (1, 'Calle Principal 123', 'Madrid', 'España', '+34 912 345 678', 1, 0);
-- Luego obtener el ID del pedido y insertar order_items

-- ========================================
-- 2. OBTENER PEDIDOS RECIENTES DE UN USUARIO
-- ========================================
SELECT
    o.order_id,
    o.order_date,
    o.total,
    ts.status_name,
    COUNT(oi.order_items_id) as item_count
FROM orders o
         JOIN tbl_status ts ON o.status_id = ts.status_id
         LEFT JOIN order_items oi ON o.order_id = oi.order_id
WHERE o.user_id = 1
GROUP BY o.order_id
ORDER BY o.order_date DESC
    LIMIT 10;

-- ========================================
-- 3. OBTENER DETALLES DE UN PEDIDO
-- ========================================
SELECT
    o.order_id,
    o.order_date,
    o.total,
    ts.status_name,
    oi.book_id,
    oi.quantity,
    oi.price,
    (oi.quantity * oi.price) as subtotal
FROM orders o
         JOIN tbl_status ts ON o.status_id = ts.status_id
         LEFT JOIN order_items oi ON o.order_id = oi.order_id
WHERE o.order_id = 1;

-- ========================================
-- 4. OBTENER ESTADO DE PAGO DE UN PEDIDO
-- ========================================
SELECT
    p.payment_id,
    p.transaction_id,
    p.amount,
    pm.method_name,
    ts.status_name as payment_status,
    p.payment_date
FROM payments p
         JOIN pay_methods pm ON p.pay_methods = pm.pay_method_id
         JOIN tbl_status ts ON p.status_id = ts.status_id
WHERE p.order_id = 1;

-- ========================================
-- 5. OBTENER HISTORIAL DE PEDIDOS DE UN USUARIO
-- ========================================
SELECT
    o.order_id,
    o.order_date,
    o.total,
    ts.status_name,
    COUNT(oi.order_items_id) as total_items
FROM orders o
         JOIN tbl_status ts ON o.status_id = ts.status_id
         LEFT JOIN order_items oi ON o.order_id = oi.order_id
WHERE o.user_id = 1
GROUP BY o.order_id
ORDER BY o.order_date DESC;

-- ========================================
-- 6. ACTUALIZAR ESTADO DE UN PEDIDO
-- ========================================
UPDATE orders
SET status_id = 2
WHERE order_id = 1;

-- ========================================
-- 7. OBTENER PEDIDOS POR RANGO DE FECHAS
-- ========================================
SELECT * FROM orders
WHERE order_date BETWEEN '2026-05-01' AND '2026-05-31'
  AND user_id = 1
ORDER BY order_date DESC;

-- ========================================
-- 8. CALCULAR TOTAL DE VENTAS POR USUARIO
-- ========================================
SELECT
    u.user_id,
    u.username,
    COUNT(o.order_id) as total_orders,
    SUM(o.total) as total_spent,
    AVG(o.total) as avg_order_value
FROM users u
         LEFT JOIN orders o ON u.user_id = o.user_id
GROUP BY u.user_id
ORDER BY total_spent DESC;