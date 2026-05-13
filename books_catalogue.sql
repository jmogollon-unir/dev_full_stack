-- Crear schema
CREATE database Books_Catalogue;
go
USE Books_Catalogue;
go
-- 1. Tabla de Usuarios

CREATE TABLE Tbl_Status (
StatusID INT primary key,
NameStatus varchar(100)     --('pending', 'processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',

);



CREATE TABLE Tbl_Method_Pay (
MethodPayID INT primary key,
NameMethod varchar(100)     --Method ENUM('credit_card', 'debit_card', 'paypal', 'bank_transfer') NOT NULL,

);

    


CREATE TABLE Users (
    UserId INT IDENTITY PRIMARY KEY,
    Username VARCHAR(100) UNIQUE NOT NULL,
    Email VARCHAR(150) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    First_Name VARCHAR(100),
    Last_Name VARCHAR(100),
    Created_at DATETIME,
    Created_by DATETIME,
    Updated_at DATETIME,
    Updated_by DATETIME
);

-- 2. Tabla de Direcciones
CREATE TABLE Addresses (
    AddressId INT IDENTITY PRIMARY KEY,
    UserId INT FOREIGN KEY REFERENCES Users(UserId) NOT NULL,
    Address VARCHAR(255) NOT NULL,
    City VARCHAR(100) NOT NULL,
    Postal_Code VARCHAR(20),
    Country VARCHAR(100),
    Phone VARCHAR(20),
    Is_Default BIT,
    Created_at DATETIME,
    Created_by INT,
    Updated_at DATETIME,
    Updated_by INT

    
);



-- 3. Tabla de Libros (Catálogo)
CREATE TABLE Books (
    BookId INT IDENTITY PRIMARY KEY,
    Title VARCHAR(255) NOT NULL,
    Author VARCHAR(255) NOT NULL,
    Isbn VARCHAR(20) UNIQUE,
    Price DECIMAL(10, 2) NOT NULL,
    Stock INT NOT NULL DEFAULT 0,
    Cover_url VARCHAR(MAX),
    Descrition VARCHAR(MAX),
    Genre VARCHAR(100),
    Formato VARCHAR(50),  --'Físico y Digital'
    Languaje VARCHAR(50),  --'Español'
    Publication_Date DATE,
    Popularity INT DEFAULT 0,
    Is_Available BIT DEFAULT 0,
    Created_at DATETIME,
    Created_by INT,
    Updated_at DATETIME,
    Updated_by INT,
);



-- 4. Tabla de Órdenes/Pedidos
CREATE TABLE Orders (
    OrderId INT IDENTITY PRIMARY KEY,
    AddressId INT FOREIGN KEY REFERENCES Addresses(AddressId),
    UserId INT FOREIGN KEY REFERENCES Users(UserId),
    StatusID INT FOREIGN KEY REFERENCES Tbl_Status(StatusID),
    Order_Date DATETIME,
    Total DECIMAL(12, 2) NOT NULL,
    Created_at DATETIME,
    Created_by INT,
    Updated_at DATETIME,
    Updated_by INT,
    --INDEX idx_user_id (user_id),
    --INDEX idx_status (status),
    --INDEX idx_order_date (order_date)
);



-- 5. Tabla de Items del Pedido
CREATE TABLE Order_Items (
    Order_Item_Id INT IDENTITY PRIMARY KEY,
    Quantity INT NOT NULL DEFAULT 1,
    Price DECIMAL(10, 2) NOT NULL,
    OrderId INT FOREIGN KEY  REFERENCES Orders(OrderId),
    BookId INT FOREIGN KEY REFERENCES Books(BookId),
    Created_at DATETIME,
    Created_by INT,
    Updated_at DATETIME,
    Updated_by INT

    --INDEX idx_order_id (order_id),
    --INDEX idx_book_id (book_id)
);

-- 6. Tabla de Pagos
CREATE TABLE Payments (
    PaymentId INT IDENTITY PRIMARY KEY,
    OrderId INT FOREIGN KEY  REFERENCES Orders(OrderId),
    Amount DECIMAL(12, 2) NOT NULL,
    MethodPayID INT FOREIGN KEY  REFERENCES Tbl_Method_Pay(MethodPayID),
    Payment_Date DATETIME,
    StatusID INT FOREIGN KEY REFERENCES Tbl_Status(StatusID),
    Transaction_Id varchar(255),
    Created_at DATETIME,
    Created_by INT,
    Updated_at DATETIME,
    Updated_by INT

    --INDEX idx_status (status),
    --INDEX idx_order_id (order_id)
);

-- 7. Tabla de Reseñas/Comentarios
CREATE TABLE Reviews (
    ReviewId INT IDENTITY PRIMARY KEY,
    BookId INT FOREIGN KEY REFERENCES Books(BookId),
    UserId INT FOREIGN KEY REFERENCES Users(UserId),
    Rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    Comment VARCHAR(MAX),
    Is_Verified_Purchase BIT DEFAULT 0,
    Review_Date DATETIME,
    Created_at DATETIME,
    Created_by INT,
    Updated_at DATETIME,
    Updated_by INT
    --INDEX idx_book_id (book_id),
    --INDEX idx_user_id (user_id),
    --INDEX idx_rating (rating)
);

-- 8. Tabla de Carrito (Sesión temporal)
CREATE TABLE Cart_Items (
    CartId INT IDENTITY PRIMARY KEY,
    BookId INT FOREIGN KEY REFERENCES Books(BookId),
    UserId INT FOREIGN KEY REFERENCES Users(UserId),
    SessionId VARCHAR(255),
    Quantity INT,
    Added_at datetime,
    Expires_at datetime NULL,
    Created_at DATETIME,
    Created_by INT,
    Updated_at DATETIME,
    Updated_by INT

    --INDEX idx_user_id (user_id),
    --INDEX idx_session_id (session_id)
);
