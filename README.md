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
┌──────────────┐
│    Users     │
├──────────────┤
│ user_id (PK) │
│ username     │
│ email        │
│ password     │
│ first_name   │
│ last_name    │
│ created_at   │
│ updated_at   │
└────────┬─────┘
         │ 1:N
         ├─────────────────────┐
         │                     │
         ▼                     ▼
   ┌───────────────┐    ┌──────────────┐
   │   Orders      │    │  Addresses   │
   ├───────────────┤    ├──────────────┤
   │ order_id (PK) │    │address_id(PK)│
   │ user_id (FK)  │    │ user_id (FK) │
   │ order_date    │    │ address      │
   │ status        │    │ city         │
   │ total         │    │ postal_code  │
   │address_id(FK) │    | country      │
   │ created_at    │    │ phone        │
   │ updated_at    │    │ is_default   │
   └────────┬──────┘    │ created_at   │
            |           └──────────────┘
            │ 1:N
            │
            ▼
      ┌──────────────┐
      │ OrderItems   │
      ├──────────────┤
      │   id (PK)    │
      │order_id (FK) │
      │ book_id (FK) │
      │ quantity     │
      │ price        │
      │ created_at   │
      └────────┬─────┘
               │ N:1
               │
               ▼
         ┌───────────────────┐
         │     Books         │
         ├───────────────────┤
         │  book_id (PK)     │
         │  title            │
         │  author           │
         │  isbn             │
         │  price            │
         │  stock            │
         │  cover_url        │
         │  description      │
         │  genre            │
         │  format           │
         │  language         │
         │  publication_date │
         │  popularity       │
         │  is_available     │
         │  created_at       │
         │  updated_at       │
         └────────┬──────────┘
                  │ 1:N
                  ├──────────────┐
                  │              │
                  ▼              ▼
         ┌──────────────────────┐  ┌─────────────────┐
         │    Reviews           │  │   Payments      │
         ├──────────────────────┤  ├─────────────────┤
         │ review_id (PK)       │  │ payment_id (PK) │
         │ book_id (FK)         │  │ order_id (FK)   │
         │ user_id (FK)         │  │ amount          │
         │ rating               │  │ method          │
         │ comment              │  │ payment_date    │
         │ is_verified_purchase │  │ status          │
         │ review_date          │  │ transaction_id  │
         │ created_at           │  │ created_at.     │
         └──────────────────────┘  └─────────────────┘

## 🛠️ Instalación y Configuración

### Requisitos
* Java 26
* docker

### Pasos

## Clonar repositorio

```bash
git clone git@github.com:jmogollon-unir/dev_full_stack.git

cd UNIR-SOFTWARE-G18-RELATOS-PAPEL
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


👥 Integrantes
Proyecto desarrollado por el Grupo 18 de la materia Desarrollo Full Stack del Máster Universitario en Ingeniería de Software y Sistemas Informáticos - UNIR.

* Julieth Camila Mogollón Bernal 
* Leonardo Cashiel Olaechea Saavedra 
* José Miguel Jamette Garrido 
* Francisco Javier Febles Jimenez
* Elsy Paola Amaya Lazo

