# Agente de atencion al cliente - Communications

Frontend estatico para la segunda parte del microservicio `communications`.

La pantalla permite:

- Buscar libros consumiendo `GET /api/books` mediante el API Gateway.
- Consultar pedidos con `GET /api/orders/{id}` y `GET /api/orders/user/{userId}`.
- Crear un pedido demo con `POST /api/orders`; al crearse, `orders` publica el evento en RabbitMQ y `communications` envia el correo de confirmacion.
- Intentar conexion WebSocket a `/ws-api/v1/communications/customer-agent`, usando la ruta ya configurada en el gateway. Si el backend WebSocket aun no existe, el agente sigue funcionando con REST.

## Uso

Desde esta carpeta:

```bash
node server.cjs
```

Abrir:

```text
http://localhost:5173
```

Antes de probar pedidos, levantar los servicios en este orden:

1. MySQL y RabbitMQ.
2. `eureka-server`.
3. `orders`.
4. `catalogue`.
5. `communications`.
6. `api-gateway`.

El gateway debe estar disponible en `http://localhost:8080`.
