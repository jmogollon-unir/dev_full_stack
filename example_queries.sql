-- Todos los libros
SELECT 
    b.book_id, b.title, b.author, b.isbn, b.price, b.stock, b.cover_url, b.description, g.name AS genero,
    b.format, b.language, b.publication_date, b.popularity, b.is_available
FROM books b
    JOIN genres g ON b.genre_id = g.genre_id;

-- Todos los pedidos/órdenes con nombre de usuario, status y desglose total
SELECT 
    o.order_id, u.username, u.email, o.status_id, ts.status_name, o.total, o.order_date, o.address, o.city, o.country, o.phone
FROM 
    orders o
    LEFT JOIN users u ON o.user_id = u.user_id
    LEFT JOIN tbl_status ts ON o.status_id = ts.status_id;

-- Todas las órdenes, con usuario, status, y total pagado, incluyendo detalles de método de pago y transacción
SELECT
    o.order_id, u.username, o.total, ts.status_name AS estado, p.amount AS monto_pagado, pm.method_name AS metodo_pago,
    p.transaction_id, p.payment_date
FROM
    orders o
    LEFT JOIN users u ON o.user_id = u.user_id
    LEFT JOIN tbl_status ts ON o.status_id = ts.status_id
    LEFT JOIN payments p ON o.order_id = p.order_id
    LEFT JOIN pay_methods pm ON p.pay_methods = pm.pay_method_id
ORDER BY o.order_id DESC;

-- Todas las reseñas con nombre de libro, usuario y comentario
SELECT 
    r.review_id, b.title AS libro, u.username AS usuario, r.rating, r.comment, r.is_verified_purchase, r.review_date
FROM 
    reviews r
    JOIN books b ON r.book_id = b.book_id
    LEFT JOIN users u ON r.user_id = u.user_id
ORDER BY r.review_date DESC;