## 🏆 Entregales actividad 3: Asincronía, autenticación y despliegue

### 🔗 Enlaces de la entrega

- [Vídeo-memoria](https://drive.google.com/file/d/1MAebXLMoEbPSoJhpinuxLEO_cuGuwWWW/view?usp=sharing)

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
├── users/                                           # Autenticación y perfil (:8086)
│   └── … (JPA + Redis sesiones, JWT)
├── communications/                                  # Correos + agente chat (:8083)
│   ├── events/ …
│   └── chat/ …
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
- **Opcional (enunciado):** lecturas desde Elasticsearch y sincronización tras escrituras en BD relacional — **no implementado** en esta rama; las consultas siguen usando MySQL.

### Microservicio Orders (`:8082`)

- Creación de pedidos.
- Consulta de pedido por ID.
- Listado de pedidos por usuario.

### Microservicio Users (`:8086`)

- Login con CIF + contraseña (MD5 en cliente).
- JWT (`accessToken`) + sesión opaca (`sessionToken`) en Redis (patrón **phantom token**).
- Perfil: `GET /api/v1/users/{cif}` con `Authorization: Bearer <sessionToken>` **a través del gateway**; el gateway valida el opaco contra `users` y reenvía el JWT en el header `accessToken`.

### API Gateway (defensa y phantom token)

- Rutas **públicas** (sin `Authorization`): `OPTIONS`, `POST /api/v1/tokens`, `POST /api/v1/tokens/{id}/renewals`, `GET /api/books` y `GET /api/books/**`, handshake WebSocket bajo `/ws-api/**`.
- Resto de rutas: el cliente envía `Authorization: Bearer <sessionToken>`. El gateway llama a `GET http://users/api/v1/tokens/{sessionToken}` (balanceo Eureka), y si la sesión es válida **elimina** `Authorization` y añade **`accessToken: <JWT>`** hacia el microservicio destino.

### Microservicio Communications (`:8083`)

- Consumo de eventos RabbitMQ al crear pedidos y envío de correo (SMTP configurable por variables de entorno).
- Agente de chat (Gemini) vía WebSocket: `ws://localhost:8080/ws-api/v1/communications/chat` (a través del Gateway).

## API REST

Todas las rutas REST se consumen a través del **API Gateway** en `http://localhost:8080`.

### Users

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/v1/tokens` | Login (`username` = CIF, `password` = MD5) → `{ accessToken, sessionToken }` |
| `POST` | `/api/v1/tokens/{sessionToken}/renewals` | Renovar sesión |
| `GET` | `/api/v1/tokens/{sessionToken}` | Validar sesión |
| `GET` | `/api/v1/users/{cif}` | Perfil (vía gateway: `Authorization: Bearer <sessionToken>`; el microservicio recibe `accessToken` con el JWT) |

**Datos de prueba** (ver `users/src/main/resources/db/schema.sql` y pedidos en `orders` para `userId` 1–3):

| CIF | Contraseña | userId pedidos |
|-----|------------|----------------|
| `B12345678` | `123456` | 1 |
| `B87654321` | `password123` | 2 |
| `B11223344` | `admin2025` | 3 |

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

Requieren pasar por el gateway con sesión: el cliente envía `Authorization: Bearer <sessionToken>`; el servicio `orders` recibe **`accessToken`** (JWT) y comprueba que el `userId` del token coincida con el cuerpo o la ruta.

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/orders` | Crear pedido (`userId` debe coincidir con el claim JWT) |
| `GET` | `/api/orders/{id}` | Obtener pedido (solo si pertenece al usuario del token) |
| `GET` | `/api/orders/user/{userId}` | Pedidos de un usuario (`userId` = claim del JWT) |

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
              ┌─────────────┼─────────────┬──────────────┐
              ▼             ▼             ▼              ▼
     Eureka Server    CATALOGUE      ORDERS         USERS
        (:8761)        (:8081)       (:8082)        (:8086)
              │             │             │              │
              │             ▼             ▼              ▼
              │      books_catalogue  books_orders   books_users
              │       (MySQL)         (MySQL)        (MySQL) + Redis
              │                           │
              │                           │ RabbitMQ
              │                           ▼
              │                   COMMUNICATIONS (:8083)
              │                           │
              └───────────────────────────┘
                              ▼
                    SMTP (correo) / Gemini (chat)
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

### 2. Opción A — Todo el backend con Docker Compose (recomendado)

Desde la raíz `dev_full_stack/`:

```bash
cp .env.example .env   # opcional: EMAIL_*, GEMINI_API_KEY
docker compose up --build
```

Esto levanta **MySQL** (puerto host **3307**), **Redis**, **RabbitMQ** (UI en **15672**), **Eureka**, **catalogue**, **orders**, **users**, **communications** y **api-gateway** (**8080**).

- El **front-end** no va en Docker: en la carpeta `../FRONT` ejecuta `npm install && npm run dev` y define `VITE_GATEWAY_URL=http://localhost:8080` (ver `FRONT/.env.example`).

### 3. Opción B — Infra en Docker y microservicios en IntelliJ

Si prefieres correr los JAR desde el IDE:

1. `docker compose up mysql redis rabbitmq` (o los contenedores equivalentes que ya uses).
2. Carga los SQL en MySQL: `books_catalogue`, `books_orders`, `books_users` (puertos locales típicos 3307 / 3308 / 3309 según `application.yml`).
3. Arranca en orden: Eureka → Orders → Catalogue → Users (con Redis) → Communications → API Gateway.

### 4. Variables útiles (Docker / local)

| Variable | Uso |
|----------|-----|
| `SPRING_DATASOURCE_URL` | JDBC (compose ya lo define para los servicios) |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | URL de Eureka |
| `REDIS_HOST` / `REDIS_PORT` | Sesiones del micro `users` |
| `RABBITMQ_*` | Pedidos → communications |
| `EMAIL_USERNAME` / `EMAIL_PASSWORD` | SMTP para correos de pedido |
| `GEMINI_API_KEY` | Agente de chat en communications |

### 5. Arranque manual (orden recomendado)

1. Infra: MySQL, Redis, RabbitMQ.
2. `EurekaServerApplication` (:8761)
3. `OrdersApplication` (:8082)
4. `CatalogueApplication` (:8081)
5. `UsersApplication` (:8086)
6. `CommunicationsApplication` (:8083)
7. `ApiGatewayApplication` (:8080)

### 6. Verificar

- API Gateway: `http://localhost:8080/api/books`, `/api/orders`, `/api/v1/tokens`
- Eureka: `http://localhost:8761`
- WebSocket agente (front): `ws://localhost:8080/ws-api/v1/communications/chat`
- Web UI de correos de prueba (MailHog): `http://localhost:8025` cuando usas `docker compose`.
- Colección Postman: `docs/Backend - Relatos Papel.postman_collection.json` (`baseUrl=http://localhost:8080`, variable `jwt` tras el login)

## Integrantes

Proyecto desarrollado por el **Grupo 18** — Desarrollo Full Stack, Máster Universitario en Ingeniería de Software y Sistemas Informáticos (UNIR).

- Julieth Camila Mogollón Bernal
- Leonardo Cashiel Olaechea Saavedra
- José Miguel Jamette Garrido
- Francisco Javier Febles Jimenez
- Elsy Paola Amaya Lazo
