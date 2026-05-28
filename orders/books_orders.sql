-- ========================================
-- CREAR SCHEMA
-- ========================================
CREATE SCHEMA IF NOT EXISTS books_orders;
USE books_orders;

-- ========================================
-- TABLA: USERS (Referencia simplificada)
-- ========================================
CREATE TABLE users (
                       user_id INT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       INDEX idx_username (username),
                       INDEX idx_email (email)
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
                           FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
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
                        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
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
-- DML: INSERTAR USUARIOS
-- ========================================
INSERT INTO users (user_id, username, email, first_name, last_name) VALUES
                                                                        (1, 'julieth_mogollon', 'julieth@example.com', 'Julieth', 'Mogollon'),
                                                                        (2, 'carlos_ruiz', 'carlos@example.com', 'Carlos', 'Ruiz'),
                                                                        (3, 'ana_lopez', 'ana@example.com', 'Ana', 'López'),
                                                                        (4, 'miguel_torres', 'miguel@example.com', 'Miguel', 'Torres'),
                                                                        (5, 'pedro_gomez', 'pedro@example.com', 'Pedro', 'Gómez'),
                                                                        (6, 'maria_sanchez', 'maria@example.com', 'María', 'Sánchez');

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