# 📚 Relatos de Papel - E-Commerce de Libros

Plataforma e-commerce moderna para la venta de libros físicos y digitales, desarrollada con **React** y **Vite**, como proyecto del Máster Universitario en Ingeniería de Software y Sistemas Informáticos de UNIR.

## 🚀 Stack Tecnológico

### Frontend

- **React 19.2.4** - Framework UI con hooks modernos
- **Vite 8.0.4** - Build tool con HMR (Hot Module Replacement)
- **React Router DOM 7.14.1** - Enrutamiento SPA
- **JavaScript ES6+** - Sintaxis moderna
- **JSX** - Templates en JavaScript
- **HTML5** y **CSS3** - Markup y estilos
- **Bootstrap 5.3.8** - Framework UI
- **Bootstrap Icons 1.13.1** - Librería de iconos
- **Tailwind CSS 4.2.4** - Utilidades CSS

### Herramientas de Desarrollo

- **ESLint 9.39.4** - Linter de código
- **Vite Plugin React 6.0.1** - Integración React + Vite
- **@types/react** - Type definitions

## 🔌 Backend (misma aplicación)

Este front vive en la carpeta `frontend/` del proyecto único `relatos-papel` y se **compila dentro del JAR de Spring Boot** (`./mvnw clean package` desde la raíz). En producción el front y la API salen del mismo servidor, por eso las llamadas usan rutas relativas (`/api/...`), sin CORS ni URLs fijas:

- Catálogo y pedidos: `src/services/api.js` → `GET /api/books`, `GET /api/books/{id}`, `POST /api/orders`, `GET /api/orders/user/{userId}`
- Login: `src/hooks/useLogin.js` → `POST /api/users/login` con `{ username: <correo>, password: <MD5> }`; la respuesta es el perfil del usuario y se guarda en `localStorage` (`rp_user`). Sin tokens.

**Desarrollo del front con recarga en caliente** (con el backend levantado en `:8080`, ver README de la raíz):

```bash
cd frontend
npm install
npm run dev        # http://localhost:5173  (Vite reenvía /api a http://localhost:8080)
```

## 📁 Estructura del Proyecto

src/
├── assets/ # Imágenes y archivos estáticos
├── components/ # Componentes reutilizables
│ ├── Footer/
│ ├── Header/
│ ├── HeaderBookDetails/
│ ├── HeaderSearch/
│ ├── Logo/
│ ├── SideCart/
├── context/ # Context API providers
│ ├── auth/ # Autenticación y usuario
│ ├── cart/ # Carrito de compras
│ └── global/ # Estado global (búsqueda)
├── services/ # Cliente HTTP hacia la API (api.js)
├── hooks/ # Custom hooks
│ ├── useBook.js # Obtiene detalles de un libro
│ ├── useBooks.js # Obtiene catálogo de libros
│ ├── useCart.js # Integración con carrito
│ └── useLogin.js # Login (correo + contraseña) y logout
├── pages/ # Páginas de la aplicación
│ ├── BookDetail/
│ ├── Cart/
│ ├── Checkout/
│ ├── Home/
│ ├── Landing/
│ ├── Login/
│ ├── Profile/
├── routes/ # Configuración de rutas
└── utils/ # Utilidades y mocks
└── mocks/
├── mockBookDetail.js
├── mockBooks.js
└── mockCompanies.js

Code

## 🎯 Funcionalidades Principales

### 1. **Catálogo de Libros**

- Listado completo con grid responsive
- Búsqueda por título y autor
- Filtrado por género y rango de precios personalizado
- Ordenamiento: precio, fecha, popularidad

### 2. **Detalle de Libro**

- Información completa del producto
- Galería de imágenes
- Sistema de reseñas de clientes
- Indicador de stock y disponibilidad
- Botón de agregar al carrito

### 3. **Carrito de Compras**

- Persistencia en localStorage
- Cálculo automático de totales
- Actualización de cantidades
- Validación de stock
- Integración con botón flotante

### 4. **Autenticación**

- Login con correo + contraseña (la contraseña se envía como MD5)
- Sesión persistente en `localStorage`
- Perfil de usuario con sus pedidos
- Rutas privadas: `/checkout` y `/profile`

## 🛠️ Instalación y Configuración

### Requisitos
* Node.js 20.19+ o 22.12+
* npm

### Pasos

## Clonar repositorio

## Instalar dependencias

```bash
npm install
```

## Configurar la API

Por defecto no hay que configurar nada (mismo origen / proxy de Vite). Solo si la API está en otro servidor, copia `.env.example` a `.env` y define `VITE_API_URL=http://localhost:8080`.

## Iniciar servidor de desarrollo

```bash
npm run dev
```

## Build para producción
```bash
npm run build
```

## Preview de build

```bash
npm run preview
```

👥 Integrantes
Proyecto desarrollado por el Grupo 18 de la materia Desarrollo Full Stack del Máster Universitario en Ingeniería de Software y Sistemas Informáticos - UNIR.

* Julieth Camila Mogollón Bernal 
* Leonardo Cashiel Olaechea Saavedra 
* José Miguel Jamette Garrido 
* Francisco Javier Febles Jimenez
* Elsy Paola Amaya Lazo
