## 🏆 Entregales actividad 2: Laboratorio. Desarrollo back-end: microservicios con Java y Spring

### Enlaces de la entrega  🔗 

#### [Vídeo-memoria]()

#### [URL del proyecto despleago]()

# 📚 Relatos de Papel - E-Commerce de Libros

Plataforma e-commerce moderna para la venta de libros físicos y digitales, desarrollada con **Java** y **SpringBoot**, como proyecto del Máster Universitario en Ingeniería de Software y Sistemas Informáticos de UNIR.

## 🚀 Stack Tecnológico

### Backend

- **Java 26**
- **SpringBoot**

### Herramientas de Desarrollo

- 

## 📁 Estructura del Proyecto


Code

## 🎯 Funcionalidades Principales

### 1. **Catálogo de Libros**

- 

### 2. **Detalle de Libro**

- 

### 3. **Carrito de Compras**

- 

### 4. **Autenticación**

- 

## Modelo entidad-relación
```mermaid
erDiagram

    ROLES ||--|{ USERS : "assigned_to"

    USERS ||--o{ ADDRESSES : "has"
    USERS ||--o{ ORDERS : "places"
    USERS ||--o{ REVIEWS : "writes"

    ORDERS ||--|{ ORDER_ITEMS : "contains"
    ORDERS ||--|{ PAYMENTS : "pays"

    BOOKS ||--o{ ORDER_ITEMS : "ordered_in"
    BOOKS ||--o{ REVIEWS : "has"

    GENRES ||--|{ BOOKS : "classifies"

    TBL_STATUS ||--|{ ORDERS : "with"
    TBL_STATUS ||--|{ PAYMENTS : "has"

    PAY_METHODS ||--|{ PAYMENTS : "used_in"

    ROLES {
        int IdRoles PK
        varchar NameRol
    }

    USERS {
        int UserId PK
        int IdRoles FK
        varchar Username
        varchar Email
        varchar Password
        varchar First_Name
        varchar Last_Name
        datetime Created_at
        datetime Created_by
        datetime Updated_at
        datetime Updated_by
    }

    ADDRESSES {
        int address_id PK
        int user_id FK
        varchar address
        varchar city
        varchar postal_code
        varchar country
        varchar phone
        boolean is_default
        datetime created_at
        datetime updated_at
    }

    GENRES {
        int genre_id PK
        varchar name
    }

    BOOKS {
        int book_id PK
        varchar title
        varchar author
        varchar isbn
        decimal price
        int stock
        varchar cover_url
        text description
        int genre_id FK
        varchar format
        varchar language
        date publication_date
        int popularity
        boolean is_available
        datetime created_at
        datetime updated_at
    }

    ORDERS {
        int order_id PK
        int user_id FK
        varchar address
        varchar city
        varchar country
        varchar phone
        int status_id FK
        decimal total
        datetime order_date
        datetime created_at
        datetime updated_at
    }

    ORDER_ITEMS {
        int order_items_id PK
        int order_id FK
        int book_id FK
        int quantity
        decimal price
        datetime created_at
    }

    TBL_STATUS {
        int status_id PK
        enum status_name
    }

    REVIEWS {
        int review_id PK
        int book_id FK
        int user_id FK
        int rating
        text comment
        boolean is_verified_purchase
        datetime review_date
        datetime created_at
    }

    PAYMENTS {
        int payment_id PK
        int order_id FK
        int pay_method_id FK
        int status_id FK
        varchar transaction_id
        decimal amount
        datetime payment_date
        datetime created_at
    }

    PAY_METHODS {
        int pay_method_id PK
        enum method_name
    }
```

## 🛠️ Instalación y Configuración

### Requisitos
* Java 26
* docker

### Pasos

## Clonar repositorio

```bash
git clone git@github.com:jmogollon-unir/dev_full_stack.git

cd dev_full_stack
```

## Crear base de datos local con docker

```bash
docker pull mysql
```

```bash
docker run -p 3306:3306 --name db-relatosdepapel -e MYSQL_ROOT_PASSWORD=mysql -d mysql:latest
```

### Configurar base desde dataGrip

#### Crear base de datos MySQL

- Crear base de datos local con usuario *root* y password *mysql*

#### Crear schema base de datos MySQL

```bash
CREATE SCHEMA IF NOT EXISTS books_catalogue;
USE books_catalogue;
```

#### Crear tablas

- Copiar y ejecuta **books_catalogue.sql** para crear las tablas de la base de datos acorde con el diagrama entidad-relación

#### Insertar datos

- Copiar y ejecuta **data.sql** para llenar las tablas de la base de datos con datos mocks

## Iniciar servidor de desarrollo desde intelliJ IDEA

- 

👥 Integrantes
Proyecto desarrollado por el Grupo 18 de la materia Desarrollo Full Stack del Máster Universitario en Ingeniería de Software y Sistemas Informáticos - UNIR.

* Julieth Camila Mogollón Bernal 
* Leonardo Cashiel Olaechea Saavedra 
* José Miguel Jamette Garrido 
* Francisco Javier Febles Jimenez
* Elsy Paola Amaya Lazo

