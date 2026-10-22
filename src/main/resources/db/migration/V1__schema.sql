-- Relatos de Papel - esquema único (MySQL 8). Ejecutado por Flyway.

-- ========================================
-- TABLA: USERS (usuarios/empresas; login por correo)
-- Tabla única compartida por users y orders.
-- ========================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(15),
    address VARCHAR(255),
    cif VARCHAR(20) UNIQUE,
    sector VARCHAR(50),
    employees INT,
    founded_year INT,
    password VARCHAR(32) NOT NULL,          -- MD5 de la contraseña
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_cif (cif)
);

-- ========================================
-- TABLA: GENRES (Catálogo)
-- ========================================
CREATE TABLE genres (
                        genre_id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL UNIQUE,
                        INDEX idx_name (name)
);

-- ========================================
-- TABLA: BOOKS (Catálogo Principal)
-- ========================================
CREATE TABLE books (
                       book_id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       isbn VARCHAR(20) NOT NULL UNIQUE,
                       price DECIMAL(10, 2) NOT NULL,
                       stock INT NOT NULL DEFAULT 0,
                       cover_url LONGTEXT,
                       description LONGTEXT,
                       genre_id INT NOT NULL,
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
-- TABLA: TBL_STATUS (Estados de Pedido)
-- ========================================
CREATE TABLE tbl_status (
                            status_id INT AUTO_INCREMENT PRIMARY KEY,
                            status_name ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') NOT NULL UNIQUE,
                            description VARCHAR(255),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- TABLA: PAY_METHODS (Métodos de Pago)
-- ========================================
CREATE TABLE pay_methods (
                             pay_method_id INT AUTO_INCREMENT PRIMARY KEY,
                             method_name ENUM('credit_card', 'debit_card', 'paypal', 'bank_transfer', 'cryptocurrency') NOT NULL UNIQUE,
                             description VARCHAR(255),
                             is_active BOOLEAN DEFAULT TRUE,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- TABLA: ADDRESSES (Direcciones de Envío)
-- ========================================
CREATE TABLE addresses (
                           address_id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT NOT NULL,
                           address VARCHAR(255) NOT NULL,
                           city VARCHAR(100) NOT NULL,
                           postal_code VARCHAR(20),
                           country VARCHAR(100),
                           phone VARCHAR(20),
                           is_default BOOLEAN DEFAULT FALSE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                           INDEX idx_user_id (user_id),
                           INDEX idx_is_default (is_default)
);

-- ========================================
-- TABLA: ORDERS (Pedidos Principales)
-- ========================================
CREATE TABLE orders (
                        order_id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        city VARCHAR(100) NOT NULL,
                        country VARCHAR(100) NOT NULL,
                        phone VARCHAR(20),
                        status_id INT NOT NULL DEFAULT 1,
                        total DECIMAL(12, 2) NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                        FOREIGN KEY (status_id) REFERENCES tbl_status(status_id) ON DELETE RESTRICT,
                        INDEX idx_user_id (user_id),
                        INDEX idx_status_id (status_id),
                        INDEX idx_order_date (order_date),
                        INDEX idx_created_at (created_at)
);

-- ========================================
-- TABLA: ORDER_ITEMS (Ítems del Pedido)
-- ========================================
CREATE TABLE order_items (
                             order_items_id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id INT NOT NULL,
                             book_id INT NOT NULL,
                             quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
                             price DECIMAL(10, 2) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                             INDEX idx_order_id (order_id),
                             INDEX idx_book_id (book_id)
);

-- ========================================
-- TABLA: PAYMENTS (Pagos)
-- ========================================
CREATE TABLE payments (
                          payment_id INT AUTO_INCREMENT PRIMARY KEY,
                          order_id INT NOT NULL UNIQUE,
                          pay_methods INT NOT NULL,
                          status_id INT NOT NULL DEFAULT 1,
                          transaction_id VARCHAR(255) UNIQUE,
                          amount DECIMAL(12, 2) NOT NULL,
                          payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                          FOREIGN KEY (pay_methods) REFERENCES pay_methods(pay_method_id) ON DELETE RESTRICT,
                          FOREIGN KEY (status_id) REFERENCES tbl_status(status_id) ON DELETE RESTRICT,
                          INDEX idx_order_id (order_id),
                          INDEX idx_status_id (status_id),
                          INDEX idx_transaction_id (transaction_id),
                          INDEX idx_payment_date (payment_date)
);
