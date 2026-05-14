-- Crear schema
CREATE SCHEMA IF NOT EXISTS books_catalogue;
USE books_catalogue;

-- 1. Tablas de Referencia
CREATE TABLE tbl_status (
    status_id INT PRIMARY KEY,
    status_name ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') NOT NULL
);

CREATE TABLE pay_methods (
    pay_method_id INT PRIMARY KEY,
    method_name ENUM('credit_card', 'debit_card', 'paypal', 'bank_transfer') NOT NULL
);

CREATE TABLE genres (
    genre_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Tabla de Usuarios
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Tabla de Direcciones
CREATE TABLE addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20),
    country VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    is_default BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 4. Tabla de Libros (Catálogo)
CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    cover_url LONGTEXT,
    description LONGTEXT,
    genre_id INT NOT NULL,
    format ENUM('Físico', 'Digital', 'Físico y Digital') DEFAULT 'Físico y Digital',
    language VARCHAR(50) DEFAULT 'Español',
    publication_date DATE,
    popularity INT DEFAULT 0,
    is_available BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE RESTRICT,
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_price (price),
    INDEX idx_publication_date (publication_date),
    INDEX idx_popularity (popularity)
);

-- 5. Tabla de Órdenes/Pedidos
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    status_id INT NOT NULL,
    total DECIMAL(12, 2) NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES tbl_status(status_id) ON DELETE RESTRICT,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status_id),
    INDEX idx_order_date (order_date)
);

-- 6. Tabla de Items del Pedido
CREATE TABLE order_items (
    order_items_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    price DECIMAL(10, 2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_book_id (book_id)
);

-- 7. Tabla de Pagos
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    pay_methods INT NOT NULL,
    status_id INT NOT NULL,
    transaction_id VARCHAR(255),
    amount DECIMAL(12, 2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (pay_methods) REFERENCES pay_methods(pay_method_id) ON DELETE RESTRICT,
    FOREIGN KEY (status_id) REFERENCES tbl_status(status_id) ON DELETE RESTRICT,
    INDEX idx_status (status_id),
    INDEX idx_order_id (order_id)
);

-- 8. Tabla de Reseñas/Comentarios
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    user_id INT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment LONGTEXT,
    is_verified_purchase BOOLEAN DEFAULT FALSE,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_book_id (book_id),
    INDEX idx_user_id (user_id),
    INDEX idx_rating (rating)
);

-- 9. Tabla de Carrito (Sesión temporal)
CREATE TABLE cart_items (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    session_id VARCHAR(255),
    quantity INT NOT NULL,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_book_id (book_id),
    INDEX idx_session_id (session_id)
);
