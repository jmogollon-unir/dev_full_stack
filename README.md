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
    USERS ||--o{ ORDERS : "places"
    USERS ||--o{ ADDRESSES : "has"
    ORDERS ||--|{ ORDERITEMS : "contains"
    BOOKS ||--o{ ORDERITEMS : "included_in"
    BOOKS ||--o{ REVIEWS : "has"
    USERS ||--o{ REVIEWS : "writes"
    ORDERS ||--|| PAYMENTS : "pays"

    USERS {
        int user_id PK
        string username
        string email
        string password
        string first_name
        string last_name
        datetime created_at
        datetime updated_at
    }
    ADDRESSES {
        int address_id PK
        int user_id FK
        string address
        string city
        string postal_code
        string country
        string phone
        boolean is_default
        datetime created_at
    }
    ORDERS {
        int order_id PK
        int user_id FK
        int address_id FK
        datetime order_date
        string status
        decimal total
        datetime created_at
        datetime updated_at
    }
    ORDERITEMS {
        int id PK
        int order_id FK
        int book_id FK
        int quantity
        decimal price
        datetime created_at
    }
    BOOKS {
        int book_id PK
        string title
        string author
        string isbn
        decimal price
        int stock
        string cover_url
        string description
        string genre
        string format
        string language
        datetime publication_date
        string popularity
        boolean is_available
        datetime created_at
        datetime updated_at
    }
    REVIEWS {
        int review_id PK
        int book_id FK
        int user_id FK
        int rating
        string comment
        boolean is_verified_purchase
        datetime review_date
        datetime created_at
    }
    PAYMENTS {
        int payment_id PK
        int order_id FK
        decimal amount
        string method
        datetime payment_date
        string status
        string transaction_id
        datetime created_at
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

