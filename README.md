# hidraulica-caacupe-backend (FULL)

Spring Boot 3.4.x + Java 17 + JPA/Hibernate + PostgreSQL + Swagger (springdoc).

## Ejecutar Postgres (Docker)
```bash
docker compose up -d
```

## Ejecutar la app
```bash
mvn spring-boot:run
```

## Swagger
- UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI: http://localhost:8080/v3/api-docs

## Auth DEV
Basic Auth (in-memory):
- admin / admin123

## Movimiento de Stock (real)
Endpoint: `POST /api/v1/movimientos-stock`

### Ejemplo ENTRADA
```json
{
  "tipo": "ENTRADA",
  "referenciaTipo": "COMPRA",
  "referenciaId": 123,
  "items": [
    { "productoId": 1, "depositoId": 1, "cantidad": 5.000 }
  ]
}
```

### Ejemplo SALIDA
```json
{
  "tipo": "SALIDA",
  "referenciaTipo": "VENTA",
  "referenciaId": 999,
  "items": [
    { "productoId": 1, "depositoId": 1, "cantidad": 2.000 }
  ]
}
```

### AJUSTE (setea cantidad absoluta)
```json
{
  "tipo": "AJUSTE",
  "items": [
    { "productoId": 1, "depositoId": 1, "cantidad": 10.000 }
  ]
}
```

## Entrega 4: Impresión PDF (Factura NO FISCAL)
- `GET /api/v1/facturas/{id}/pdf` -> devuelve `application/pdf` (inline)
- Para Factura FISCAL: endpoint existe pero por ahora responde error (base preparada).

Sugerencia: emitir una factura NO_FISCAL desde una venta confirmada y luego abrir el PDF en el navegador.

### PDF NO FISCAL (formato ticket recomendado)
- `GET /api/v1/facturas/{id}/pdf` (por defecto `format=TICKET`)
- `GET /api/v1/facturas/{id}/pdf?format=A4`
Empresa en el encabezado: Hidráulica Nuestra Sra. De Caacupe

## Entrega 5: Seguridad real (Usuarios en BD + Roles/Permisos + JWT)
### Login JWT
- `POST /api/auth/login`
Body:
```json
{ "username": "admin", "password": "admin123" }
```
Respuesta:
- `token` (usar en headers: `Authorization: Bearer <token>`)

### Admin (solo ROLE_ADMIN)
- `GET /api/v1/admin/usuarios?page=0&size=20`
- `POST /api/v1/admin/usuarios`
- `PUT /api/v1/admin/usuarios/{id}`
- `POST /api/v1/admin/usuarios/{id}/reset-password`

Notas:
- Roles seed: ADMIN, CAJERO, TALLER
- Usuario admin inicial configurable en application.yml bajo `app.seed.*`.
