## 🏆 Entregales actividad 3: Asincronía, autenticación y despliegue

### 🔗 Enlaces de la entrega

- [Vídeo-memoria]()

## 🏆 Entregales actividad 2: Laboratorio. Desarrollo back-end: microservicios con Java y Spring

### 🔗 Enlaces de la entrega

- [Vídeo-memoria](https://drive.google.com/file/d/1r45OSEIQ59PlZ8G3o-1FblemI8RPu-XU/view?usp=sharing)

---

# 📚 Relatos de Papel - E-Commerce de Libros

Plataforma e-commerce moderna para la venta de libros físicos y digitales, desarrollada con **Java** y **Spring Boot** como proyecto del Máster Universitario en Ingeniería de Software y Sistemas Informáticos (UNIR).

Arquitectura de **microservicios** independientes (`catalogue`, `orders`, `users` y `communications`), cada uno con su propia responsabilidad, registro en **Eureka** y exposición unificada por **API Gateway** (`:8080`).

## 🚀 Stack Tecnológico

| Capa | Tecnología |
|------|------------|
| Lenguaje | Java 25 |
| Framework | Spring Boot 4.0.6, Spring Cloud 2025.1.1 |
| Persistencia | Spring Data JPA, Hibernate, MySQL |
| Microservicios | Netflix Eureka, Spring Cloud Gateway |
| Mensajería | RabbitMQ (eventos de pedidos → correos) |
| Build | Maven (`mvnw` incluido) |
| Base de datos | MySQL 8 (Docker) |

## 📁 Estructura del Proyecto

```text
.
├── eureka-server/                                   # Registro de servicios (:8761)
├── catalogue/                                       # Microservicio de catálogo (:8081)
│   ├── controller/
│   │   └── model/
│   ├── exception/
│   ├── repository/
│   │   └── model/
│   ├── service/
│   └── src/main/resources/db/books_catalogue.sql
├── orders/                                          # Microservicio de pedidos (:8082)
│   ├── config/
│   ├── controller/
│   │   └── model/
│   ├── event/
│   │   ├── model/
│   │   └── service/
│   ├── exception/
│   ├── facade/
│   │   └── model/
│   ├── repository/
│   │   └── model/
│   └── service/
├── communications/                                  # Notificaciones (:8083)
│   ├── events/
│   │   ├── config/
│   │   ├── listener/
│   │   ├── model/
│   │   └── service/
├── api-gateway/                                     # Punto de entrada único (:8080)
├── docs/
│   ├── books_catalogue.png
│   ├── books_orders.png
│   └── Backend - Relatos Papel.postman_collection.json
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

### Microservicio Communications (`:8083`)

- Consumo de eventos RabbitMQ al crear pedidos.
- Envío de correo de confirmación al usuario.

## API REST

Todas las rutas REST se consumen a través del **API Gateway** en `http://localhost:8080`.

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
                            │  :8080
                            ▼
                   ┌─────────────────┐
                   │   API Gateway   │
                   └────────┬────────┘
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
     Eureka Server    CATALOGUE      ORDERS
        (:8761)        (:8081)       (:8082)
              │             │             │
              │             ▼             ▼
              │      books_catalogue  books_orders
              │       (MySQL :3307)  (MySQL :3308)
              │                           │
              │                           │ RabbitMQ
              │                           ▼
              │                   COMMUNICATIONS (:8083)
              │                           │
              └───────────────────────────┘
                              ▼
                         Gmail SMTP
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
Usuario: `root` / Contraseña: `mysql`

**Catalogue:**

- Con ayuda del file **catalogue/db/books_catalogue.sql** se pueden crear las tablas de la base de datos y completar con datos de mocks

**Orders:**

- Con ayuda del file **orders/db/books_orders.sql** se pueden crear las tablas de la base de datos y completar con datos de mocks

### 4. Configuración de aplicación

Credenciales por defecto en `application.yml` de cada microservicio:

| Servicio | JDBC URL / Infra | Puerto app |
|----------|------------------|------------|
| catalogue | `jdbc:mysql://localhost:3307/books_catalogue` | 8081 |
| orders | `jdbc:mysql://localhost:3308/books_orders` | 8082 |
| communications | RabbitMQ `:5672` + SMTP Gmail | 8083 |
| api-gateway | Eureka | 8080 |

### 5. Configuración de servicio de envío de correos 

```bash
docker run -it --name rabbitmq -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=dwfs -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:latest
```

#### Habilitar panel de administración (dentro del contenedor)

```bash
rabbitmq-plugins enable rabbitmq_management
```

Panel RabbitMQ: `http://localhost:15672/` — usuario `dwfs` / contraseña `admin`

#### Correo
- user: grupo18.unir2026@gmail.com
- pass: Grupo18.admin

### 6. Arrancar los microservicios

**Orden de arranque recomendado:** en IntelliJ IDEA:

1. MySQL (Docker) + RabbitMQ (Docker)
2. `EurekaServerApplication` (:8761)
3. `OrdersApplication` (:8082)
4. `CatalogueApplication` (:8081)
5. `CommunicationsApplication` (:8083)
6. `ApiGatewayApplication` (:8080)

### 6. Verificar

- REST vía Gateway: `http://localhost:8080/api/...`

## Integrantes

Proyecto desarrollado por el **Grupo 18** — Desarrollo Full Stack, Máster Universitario en Ingeniería de Software y Sistemas Informáticos (UNIR).

- Julieth Camila Mogollón Bernal
- Leonardo Cashiel Olaechea Saavedra
- José Miguel Jamette Garrido
- Francisco Javier Febles Jimenez
- Elsy Paola Amaya Lazo
