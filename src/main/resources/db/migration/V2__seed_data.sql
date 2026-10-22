-- Relatos de Papel - datos de ejemplo. Ejecutado por Flyway.

-- ========================================
-- DML: USUARIOS  (login: correo + contraseña)
--   techstore@unir.net     / 123456
--   digitaloffice@unir.net / password123
--   innovacorp@unir.net    / admin2025
-- Los usuarios 4-6 son clientes de ejemplo de los pedidos (contraseña: 123456).
-- ========================================
INSERT INTO users (id, name, email, phone, address, cif, sector, employees, founded_year, password) VALUES
(1, 'TechStore Madrid', 'techstore@unir.net', '+34 91 123 4567', 'Calle Gran Vía, 28, 28013 Madrid', 'B12345678', 'Tecnología', 45, 2018, MD5('123456')),
(2, 'Digital Office Solutions', 'digitaloffice@unir.net', '+34 93 987 6543', 'Passeig de Gràcia, 101, 08008 Barcelona', 'B87654321', 'Consultoría IT', 78, 2015, MD5('password123')),
(3, 'InnovaCorp Sistemas', 'innovacorp@unir.net', '+34 94 456 7890', 'Alameda de Recalde, 27, 48009 Bilbao', 'B11223344', 'Software Development', 32, 2020, MD5('admin2025')),
(4, 'Miguel Torres', 'miguel@example.com', '+34 934 567 890', 'Ronda Sant Antoni 101, Barcelona', NULL, NULL, NULL, NULL, MD5('123456')),
(5, 'Pedro Gómez', 'pedro@example.com', '+34 954 678 901', 'Calle Mayor 202, Sevilla', NULL, NULL, NULL, NULL, MD5('123456')),
(6, 'María Sánchez', 'maria@example.com', '+34 916 789 012', 'Gran Vía 303, Madrid', NULL, NULL, NULL, NULL, MD5('123456'));

-- ========================================
-- DML: INSERTAR GÉNEROS
-- ========================================
INSERT INTO genres (name) VALUES
                              ('Realismo Mágico'),
                              ('Ciencia Ficción'),
                              ('Fábula'),
                              ('Clásico'),
                              ('Romance'),
                              ('Ficción'),
                              ('Fantasía'),
                              ('Literatura Experimental'),
                              ('Misterio'),
                              ('Drama');

-- ========================================
-- DML: INSERTAR LIBROS
-- ========================================
INSERT INTO books (title, author, isbn, price, stock, cover_url, description, genre_id, format, language, publication_date, popularity, is_available) VALUES
                                                                                                                                                          ('Cien Años de Soledad', 'Gabriel García Márquez', '978-0-06-085496-7', 24.99, 15, 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Cien_a%C3%B1os_de_soledad.png/500px-Cien_a%C3%B1os_de_soledad.png', 'Una obra maestra de la literatura latinoamericana que narra la historia de la familia Buendía a lo largo de siete generaciones en el pueblo ficticio de Macondo.', 1, 'AMBOS', 'Español', '1967-05-30', 95, TRUE),
                                                                                                                                                          ('1984', 'George Orwell', '978-0-451-52494-2', 19.99, 23, 'https://upload.wikimedia.org/wikipedia/commons/5/51/1984_first_edition_cover.jpg', 'Una distopía que describe un mundo totalitario donde el Gran Hermano todo lo ve. Winston Smith trabaja en el Ministerio de la Verdad, reescribiendo la historia.', 2, 'AMBOS', 'Español', '2020-06-08', 90, TRUE),
                                                                                                                                                          ('El Principito', 'Antoine de Saint-Exupéry', '978-0-15-603469-8', 15.99, 0, 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Pitit_Prince_17.jpg/500px-Pitit_Prince_17.jpg', 'Un cuento poético que reflexiona sobre la naturaleza humana y las relaciones. Un pequeño príncipe viaja de planeta en planeta conociendo personajes peculiares.', 3, 'FISICO', 'Inglés', '1943-04-06', 85, FALSE),
                                                                                                                                                          ('Don Quijote de la Mancha', 'Miguel de Cervantes', '978-0-06-074547-4', 29.99, 8, 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Title_page_first_edition_Don_Quijote.jpg/500px-Title_page_first_edition_Don_Quijote.jpg', 'La obra cumbre de la literatura española, una historia de aventuras y caballería. Don Quijote sale en busca de aventuras acompañado de su fiel escudero.', 4, 'FISICO', 'Español', '1605-01-16', 92, TRUE),
                                                                                                                                                          ('Orgullo y Prejuicio', 'Jane Austen', '978-0-14-118173-0', 21.99, 12, 'https://upload.wikimedia.org/wikipedia/commons/d/d4/PrideandPrejudiceCH3detail.jpg', 'Una novela romántica que critica las convenciones sociales de la Inglaterra del siglo XIX. Elizabeth Bennet debe navegar las expectativas sociales.', 5, 'AMBOS', 'Español', '1813-01-28', 88, TRUE),
                                                                                                                                                          ('El Alquimista', 'Paulo Coelho', '978-0-06-250335-9', 18.99, 20, 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Bibliotheca_Chemica_Curiosa_%28Pagina_titularis%29.jpg/500px-Bibliotheca_Chemica_Curiosa_%28Pagina_titularis%29.jpg', 'Una fábula sobre seguir nuestros sueños y escuchar al corazón. Santiago, un joven pastor andaluz, emprende un viaje hacia las pirámides de Egipto.', 6, 'AMBOS', 'Español', '1988-01-01', 80, TRUE),
                                                                                                                                                          ('Harry Potter y la Piedra Filosofal', 'J.K. Rowling', '978-0-439-13959-0', 22.99, 30, 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Harry_Potter.jpg/960px-Harry_Potter.jpg', 'El comienzo de la saga mágica que conquistó al mundo entero. Harry descubre en su undécimo cumpleaños que es un mago y comienza su educación.', 7, 'AMBOS', 'Español', '2026-06-26', 100, TRUE),
                                                                                                                                                          ('Rayuela', 'Julio Cortázar', '978-0-14-118368-0', 26.99, 5, 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Rayuela_JC.png/500px-Rayuela_JC.png', 'Una novela experimental que rompe con la estructura narrativa tradicional. El lector puede elegir diferentes caminos de lectura.', 8, 'FISICO', 'Español', '1963-01-28', 75, TRUE),
                                                                                                                                                          ('El Nombre de la Rosa', 'Umberto Eco', '978-0-15-603400-0', 27.99, 10, 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/La_Sacra_ammantata_dalla_neve.jpg/960px-La_Sacra_ammantata_dalla_neve.jpg', 'Un thriller medieval que combina misterio, filosofía y semiótica. En una abadía benedictina se investiga una serie de misteriosas muertes.', 9, 'AMBOS', 'Español', '1980-09-01', 85, TRUE),
                                                                                                                                                          ('La Sombra del Viento', 'Carlos Ruiz Zafón', '978-0-06-250399-9', 23.99, 18, 'https://upload.wikimedia.org/wikipedia/commons/1/1a/Shadow_Cosplay_Photograph_from_Comic-Con_International_San_Diego.jpg', 'Una historia de amor, intriga y libros en la Barcelona de posguerra. Daniel descubre un libro que cambiará su vida en el Cementerio de los Libros Olvidados.', 9, 'AMBOS', 'Español', '2001-04-12', 50, TRUE),
                                                                                                                                                          ('El Señor de los Anillos', 'J.R.R. Tolkien', '978-0-544-00346-1', 34.99, 25, 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/El_Se%C3%B1or_de_los_Anillos_lectura.jpg/960px-El_Se%C3%B1or_de_los_Anillos_lectura.jpg', 'La épica aventura que definió el género de fantasía moderna. Frodo Bolsón debe destruir el Anillo Único.', 7, 'AMBOS', 'Español', '1954-07-29', 98, TRUE);

-- ========================================
-- DML: INSERTAR ESTADOS
-- ========================================
INSERT INTO tbl_status (status_name, description) VALUES
                                                      ('pending', 'Pedido pendiente de procesamiento'),
                                                      ('processing', 'Pedido en proceso de preparación'),
                                                      ('shipped', 'Pedido enviado'),
                                                      ('delivered', 'Pedido entregado'),
                                                      ('cancelled', 'Pedido cancelado');

-- ========================================
-- DML: INSERTAR MÉTODOS DE PAGO
-- ========================================
INSERT INTO pay_methods (method_name, description, is_active) VALUES
                                                                  ('credit_card', 'Tarjeta de crédito', TRUE),
                                                                  ('debit_card', 'Tarjeta de débito', TRUE),
                                                                  ('paypal', 'PayPal', TRUE),
                                                                  ('bank_transfer', 'Transferencia bancaria', TRUE),
                                                                  ('cryptocurrency', 'Criptomoneda', FALSE);

-- ========================================
-- DML: INSERTAR DIRECCIONES
-- ========================================
INSERT INTO addresses (user_id, address, city, postal_code, country, phone, is_default) VALUES
                                                                                            (1, 'Calle Principal 123', 'Madrid', '28001', 'España', '+34 912 345 678', TRUE),
                                                                                            (2, 'Avenida Central 456', 'Barcelona', '08002', 'España', '+34 933 456 789', TRUE),
                                                                                            (3, 'Paseo del Prado 789', 'Madrid', '28014', 'España', '+34 915 678 901', TRUE),
                                                                                            (4, 'Ronda Sant Antoni 101', 'Barcelona', '08011', 'España', '+34 934 567 890', TRUE),
                                                                                            (5, 'Calle Mayor 202', 'Sevilla', '41001', 'España', '+34 954 678 901', TRUE),
                                                                                            (6, 'Gran Vía 303', 'Madrid', '28013', 'España', '+34 916 789 012', TRUE);

-- ========================================
-- DML: INSERTAR PEDIDOS
-- ========================================
INSERT INTO orders (user_id, address, city, country, phone, status_id, total, order_date) VALUES
                                                                                              (1, 'Calle Principal 123', 'Madrid', 'España', '+34 912 345 678', 4, 89.97, '2026-05-01 10:30:00'),
                                                                                              (2, 'Avenida Central 456', 'Barcelona', 'España', '+34 933 456 789', 2, 56.98, '2026-05-10 14:15:00'),
                                                                                              (3, 'Paseo del Prado 789', 'Madrid', 'España', '+34 915 678 901', 1, 76.96, '2026-05-15 09:45:00'),
                                                                                              (1, 'Calle Principal 123', 'Madrid', 'España', '+34 912 345 678', 4, 45.98, '2026-05-20 16:20:00'),
                                                                                              (4, 'Ronda Sant Antoni 101', 'Barcelona', 'España', '+34 934 567 890', 3, 104.97, '2026-05-22 11:00:00');

-- ========================================
-- DML: INSERTAR ÍTEMS DEL PEDIDO
-- ========================================
INSERT INTO order_items (order_id, book_id, quantity, price) VALUES
-- Pedido 1
(1, 1, 2, 24.99),
(1, 7, 1, 22.99),
-- Pedido 2
(2, 2, 2, 19.99),
(2, 5, 1, 21.99),
-- Pedido 3
(3, 4, 2, 29.99),
(3, 6, 1, 18.99),
-- Pedido 4
(4, 9, 1, 27.99),
(4, 10, 1, 23.99),
-- Pedido 5
(5, 7, 2, 22.99),
(5, 11, 1, 34.99),
(5, 1, 1, 24.99);

-- ========================================
-- DML: INSERTAR PAGOS
-- ========================================
INSERT INTO payments (order_id, pay_methods, status_id, transaction_id, amount, payment_date) VALUES
                                                                                                  (1, 1, 4, 'TXN_001_20260501', 89.97, '2026-05-01 10:35:00'),
                                                                                                  (2, 2, 2, 'TXN_002_20260510', 56.98, '2026-05-10 14:20:00'),
                                                                                                  (3, 3, 1, 'TXN_003_20260515', 76.96, '2026-05-15 09:50:00'),
                                                                                                  (4, 1, 4, 'TXN_004_20260520', 45.98, '2026-05-20 16:25:00'),
                                                                                                  (5, 4, 2, 'TXN_005_20260522', 104.97, '2026-05-22 11:05:00');
