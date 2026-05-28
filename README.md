## 🏆 Entregales actividad 2: Laboratorio. Desarrollo back-end: microservicios con Java y Spring

### 🔗 Enlaces de la entrega

- [Vídeo-memoria]()

---

# 📚 Relatos de Papel - E-Commerce de Libros

Plataforma e-commerce moderna para la venta de libros físicos y digitales, desarrollada con **Java** y **Spring Boot** como proyecto del Máster Universitario en Ingeniería de Software y Sistemas Informáticos (UNIR).

Arquitectura de **dos microservicios** independientes (`catalogue` y `orders`), cada uno con su propia base de datos MySQL, registro en **Eureka** y comunicación entre servicios.

## 🚀 Stack Tecnológico

| Capa | Tecnología |
|------|------------|
| Lenguaje | Java 25 |
| Framework | Spring Boot 4.0.6, Spring Cloud 2025.1.1 |
| Persistencia | Spring Data JPA, Hibernate, MySQL |
| Microservicios | Netflix Eureka |
| Build | Maven (`mvnw` incluido) |
| Base de datos | MySQL 8 (Docker) |

## 📁 Estructura del Proyecto

```text
.
├── eureka-server/
│   └── src/main/java/...
├── catalogue/                 # Microservicio de catálogo (:8081)
│   ├── books_catalogue.sql    # DDL + datos de prueba
│   ├── example_queries.sql    # DML
│   └── src/main/java/.../+/
├── orders/                    # Microservicio de pedidos (:8082)
│   ├── books_orders.sql.      # DDL + datos de prueba
│   ├── example_queries.sql    # DML
│   └── src/main/java/.../orders/
├── docs/
│   ├── books_catalogue.png    # Diagrama ER catalogue
│   ├── books_orders.png       # Diagrama ER orders
│   └── Backend - Relatos Papel.postman_collection.json
├── api-gateway/
│   └── src/main/java/...
└── README.md
```

## 🎯 Funcionalidades Principales

### Microservicio Catalogue (`:8081`)

- CRUD de libros con géneros.
- Búsqueda por título, autor, ISBN, categoría, rating, popularidad y disponibilidad.

### Microservicio Orders (`:8082`)

- Creación de pedidos.
- Consulta de pedido por ID.
- Listado de pedidos por usuario.

## API REST

### Catalogue

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/books` | Buscar libros (query params opcionales) |
| `GET` | `/api/books/{id}` | Obtener libro por ID |
| `POST` | `/api/books` | Crear libro |
| `PUT` | `/api/books/{id}` | Reemplazar libro |
| `PATCH` | `/api/books/{id}` | Actualizar libro parcialmente |
| `DELETE` | `/api/books/{id}` | Eliminar libro |

**Query params de búsqueda:** `title`, `author`, `isbn`, `category`, `popularity`.

### Orders

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/orders` | Crear pedido |
| `GET` | `/api/orders/{id}` | Obtener pedido por ID |
| `GET` | `/api/orders/user/{userId}` | Pedidos de un usuario |

Colección Postman: `docs/Backend - Relatos Papel.postman_collection.json`.

## 🔄 Comunicación entre microservicios

```text
┌─────────────────────────────────────────────────────────────┐
│                          FRONTEND                           │
└───────────────────────────┬─────────────────────────────────┘
                     Eureka Server (:8761)
              ┌─────────────┴─────────────┐
              ▼                           ▼
   ┌──────────────────────┐    ┌──────────────────────┐
   │  CATALOGUE  :8081    │    │   ORDERS  :8082      │
   │  /api/books          │◄───│  /api/orders         │
   └──────────┬───────────┘    └──────────┬───────────┘
              │                           │
              ▼                           ▼
     books_catalogue              books_orders
     (MySQL :3307)                (MySQL :3308)
              │                           │
              └───────────┬───────────────┘
                          ▼
                  Gateway (:8080)
```

## Modelo de datos

### Catalogue

![Diagrama ER Catalogue](docs/books_catalogue.png)

### Orders

![Diagrama ER Orders](docs/books_orders.png)

## Instalación y configuración

### Requisitos

- Java 25+
- Docker

### 1. Clonar el repositorio

```bash
git clone git@github.com:jmogollon-unir/dev_full_stack.git
cd dev_full_stack
```

### 2. Bases de datos MySQL con Docker

```bash
docker pull mysql:latest

# Catalogue → puerto host 3307
docker run -p 3307:3306 --name books_catalogue -e MYSQL_ROOT_PASSWORD=mysql -d mysql:latest

# Orders → puerto host 3308
docker run -p 3308:3306 --name books_orders -e MYSQL_ROOT_PASSWORD=mysql -d mysql:latest
```

### 3. Cargar esquema y datos

Desde la raíz del proyecto (o con DataGrip / MySQL Workbench):

**Catalogue:**

- Con ayuda del file **catalogue/books_catalogue.sql** se pueden crear las tablas de la base de datos y completar con datos de mocks

**Orders:**

- Con ayuda del file **orders/books_orders.sql** se pueden crear las tablas de la base de datos y completar con datos de mocks

### 4. Configuración de aplicación

Credenciales por defecto en `application.yml` de cada microservicio:

| Servicio | JDBC URL | Puerto app |
|----------|----------|------------|
| catalogue | `jdbc:mysql://localhost:3307/books_catalogue` | 8081 |
| orders | `jdbc:mysql://localhost:3308/books_orders` | 8082 |

Usuario: `root` / Contraseña: `mysql`

### 5. Arrancar los microservicios

- Ejecución de Eureka `EurekaServerApplication`
- Ejecutar `CatalogueApplication` y `OrdersApplication` desde IntelliJ IDEA.
- También ejecutar `ApiGatewayApplication` desde IntelliJ IDEA.

### 6. Verificar

- `http://localhost:8080/api/...`

## Integrantes

Proyecto desarrollado por el **Grupo 18** — Desarrollo Full Stack, Máster Universitario en Ingeniería de Software y Sistemas Informáticos (UNIR).

- Julieth Camila Mogollón Bernal
- Leonardo Cashiel Olaechea Saavedra
- José Miguel Jamette Garrido
- Francisco Javier Febles Jimenez
- Elsy Paola Amaya Lazo
