-- ========================================
-- CREAR SCHEMA
-- ========================================
CREATE SCHEMA IF NOT EXISTS books_catalogue;
USE books_catalogue;

-- ========================================
-- TABLA: GENRES (Catálogo)
-- ========================================
CREATE TABLE genres (
                        genre_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL UNIQUE,
                        INDEX idx_name (name)
);

-- ========================================
-- TABLA: BOOKS (Catálogo Principal)
-- ========================================
CREATE TABLE books (
                       book_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       isbn VARCHAR(20) NOT NULL UNIQUE,
                       price DECIMAL(10, 2) NOT NULL,
                       stock INT NOT NULL DEFAULT 0,
                       cover_url LONGTEXT,
                       description LONGTEXT,
                       genre_id BIGINT NOT NULL,
                       format VARCHAR(255) DEFAULT 'AMBOS', -- Mapea el Enum BookFormat.AMBOS por defecto
                       language VARCHAR(50) DEFAULT 'Español',
                       publication_date DATE,
                       popularity INT DEFAULT 0,
                       is_available BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE RESTRICT,
                       INDEX idx_title (title),
                       INDEX idx_author (author),
                       INDEX idx_price (price),
                       INDEX idx_publication_date (publication_date),
                       INDEX idx_popularity (popularity)
);

-- ========================================
-- TABLA: REVIEWS (Reseñas - Información de Catálogo)
-- ========================================
CREATE TABLE reviews (
                         review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         book_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
                         comment LONGTEXT,
                         is_verified_purchase BOOLEAN DEFAULT FALSE,
                         review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
                         INDEX idx_book_id (book_id),
                         INDEX idx_user_id (user_id),
                         INDEX idx_rating (rating)
);

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
-- DML: INSERTAR RESEÑAS
-- ========================================
INSERT INTO reviews (book_id, user_id, rating, comment, is_verified_purchase, review_date) VALUES
                                                                                               (1, 1, 5, 'Una obra maestra absoluta. La narrativa de García Márquez es simplemente magistral.', TRUE, '2026-04-19'),
                                                                                               (1, 2, 4, 'Me encantó la mezcla de lo mágico y lo real. Un libro que te atrapa desde la primera página.', TRUE, '2026-05-01'),
                                                                                               (2, 3, 5, 'Una lectura inquietante pero esencial. Orwell realmente sabía cómo crear una atmósfera opresiva.', TRUE, '2026-04-22'),
                                                                                               (2, 4, 4, 'Me hizo reflexionar sobre la sociedad actual y el poder de los medios de comunicación.', TRUE, '2026-05-03'),
                                                                                               (4, 5, 5, 'Una obra monumental que combina humor, tragedia y una profunda reflexión sobre la naturaleza humana.', TRUE, '2026-04-28'),
                                                                                               (4, 6, 5, 'Me encantó la riqueza de los personajes y la sátira social. Un clásico que merece ser leído por todos.', TRUE, '2026-05-07'),
                                                                                               (6, 1, 4, 'Una historia inspiradora que me motivó a perseguir mis propios sueños.', TRUE, '2026-05-03'),
                                                                                               (6, 2, 3, 'Me gustó la simplicidad del mensaje, aunque esperaba algo más profundo.', TRUE, '2026-05-12'),
                                                                                               (7, 3, 5, 'Una historia mágica que me hizo soñar despierta. La imaginación de Rowling es increíble.', TRUE, '2026-05-05'),
                                                                                               (7, 4, 5, 'Me encantó la construcción del mundo y los personajes. Un libro que se disfruta a cualquier edad.', TRUE, '2026-05-15'),
                                                                                               (8, 6, 4, 'Una lectura desafiante pero gratificante. La estructura no lineal me hizo reflexionar sobre la narrativa.', TRUE, '2026-05-07'),
                                                                                               (8, 5, 3, 'Me gustó la experimentación, aunque a veces me perdía entre los saltos temporales.', TRUE, '2026-05-17'),
                                                                                               (9, 1, 5, 'Una historia fascinante que combina un misterio intrigante con una profunda reflexión filosófica.', TRUE, '2026-05-01'),
                                                                                               (9, 4, 5, 'Me encantó la ambientación medieval y la complejidad de los personajes. Un libro que se disfruta en cada lectura.', TRUE, '2026-05-10'),
                                                                                               (10, 2, 4, 'Una historia envolvente con una atmósfera única. Me encantó la mezcla de misterio y romance.', TRUE, '2026-05-12'),
                                                                                               (10, 1, 3, 'Me gustó la ambientación de Barcelona y la trama intrigante, aunque esperaba un poco más de profundidad en los personajes.', TRUE, '2026-05-22'),
                                                                                               (11, 4, 5, 'Una historia épica que me hizo soñar con mundos fantásticos. La riqueza del universo de Tolkien es impresionante.', TRUE, '2026-05-15');
