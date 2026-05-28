-- ========================================
-- 1. BÚSQUEDA POR TÍTULO
-- ========================================
SELECT * FROM books
WHERE is_visible = TRUE
  AND title LIKE '%Harry%'
ORDER BY popularity DESC;

-- ========================================
-- 2. BÚSQUEDA POR AUTOR
-- ========================================
SELECT * FROM books
WHERE is_visible = TRUE
  AND author LIKE '%García Márquez%';

-- ========================================
-- 3. BÚSQUEDA POR GÉNERO
-- ========================================
SELECT b.* FROM books b
                    JOIN genres g ON b.genre_id = g.genre_id
WHERE is_visible = TRUE
  AND g.name = 'Fantasía'
ORDER BY b.popularity DESC;

-- ========================================
-- 4. BÚSQUEDA POR ISBN
-- ========================================
SELECT * FROM books
WHERE is_visible = TRUE
  AND isbn = '978-0-439-13959-0';

-- ========================================
-- 5. BÚSQUEDA POR FECHA DE PUBLICACIÓN
-- ========================================
SELECT * FROM books
WHERE is_visible = TRUE
  AND publication_date BETWEEN '1980-01-01' AND '2000-12-31'
ORDER BY publication_date DESC;

-- ========================================
-- 6. VALIDAR DISPONIBILIDAD DE LIBRO (Para Orders)
-- ========================================
SELECT book_id, title, price, stock, is_available, is_visible
FROM books
WHERE book_id = 1
  AND is_visible = TRUE
  AND is_available = TRUE
  AND stock > 0;

-- ========================================
-- 7. ACTUALIZAR STOCK DESPUÉS DE COMPRA
-- ========================================
UPDATE books
SET stock = stock - 2
WHERE book_id = 1 AND stock >= 2;