# Sonido Vivo – Sistema de gestión de tienda

Proyecto académico (Desarrollo Fullstack II). Aplicación web para gestionar productos, clientes y ventas de una tienda de instrumentos musicales.

- **Backend:** Spring Boot 3 + Spring Data JPA + MySQL + Flyway (carpeta `tienda`)
- **Seguridad:** autenticación con JWT y roles `USER` y `ADMIN` (Spring Security)
- **Documentación de la API:** Swagger / OpenAPI
- **Frontend:** React con Axios (carpeta `frontend`)

## Requisitos
- Java 17
- XAMPP con MySQL encendido (puerto 3306, usuario `root` sin contraseña)
- Node.js (para el frontend)

## Cómo ejecutar el backend
1. Enciende MySQL desde XAMPP. La base `tienda_sonido_vivo` se crea sola.
2. Desde la carpeta `tienda`:
   ```
   .\mvnw.cmd spring-boot:run
   ```
   (en Linux o Mac: `./mvnw spring-boot:run`)
3. El backend queda en `http://localhost:8081`.

Al arrancar, Flyway crea las tablas (`cliente`, `producto`, `venta`, `usuario`) y la aplicación crea los usuarios de prueba si no existen.

## Swagger
Documentación y pruebas de los endpoints: `http://localhost:8081/swagger-ui/index.html`

Cómo probar con autenticación:
1. Abre `POST /api/auth/login` → Try it out y envía un usuario de prueba.
2. Copia el valor de `token` de la respuesta.
3. Presiona el botón **Authorize**, pega solo el token y confirma.
4. Ya puedes ejecutar los demás endpoints.

## Usuarios de prueba
| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin1234` | ADMIN |
| `user` | `user1234` | USER |

Las contraseñas se guardan encriptadas con BCrypt.

## Autenticación y permisos
El login (`POST /api/auth/login`) devuelve un token JWT que incluye el rol del usuario. El token se envía en el encabezado `Authorization: Bearer <token>`.

| Operación | Sin token | USER | ADMIN |
|---|---|---|---|
| Login y Swagger | Permitido | Permitido | Permitido |
| `GET /api/**` (consultar) | 401 | Permitido | Permitido |
| `POST`, `PUT`, `DELETE` en `/api/**` | 401 | 403 | Permitido |

## Endpoints
| Recurso | Ruta base |
|---|---|
| Autenticación | `/api/auth` |
| Productos | `/api/productos` |
| Clientes | `/api/clientes` |
| Ventas | `/api/ventas` |

Cada recurso ofrece el CRUD completo:

| Operación | Método y ruta | Respuesta |
|---|---|---|
| Listar todos | `GET /api/{recurso}` | 200 |
| Buscar por ID | `GET /api/{recurso}/{id}` | 200, o 404 si no existe |
| Crear | `POST /api/{recurso}` | 201 |
| Actualizar | `PUT /api/{recurso}/{id}` | 200, o 404 si no existe |
| Eliminar | `DELETE /api/{recurso}/{id}` | 204, o 404 si no existe |

## Validaciones y errores
| Código | Cuándo ocurre |
|---|---|
| 400 | Datos inválidos (campos vacíos, precio o stock negativo, RUT sin formato `12345678-9`, JSON mal formado). La respuesta indica el detalle de cada campo |
| 401 | Falta el token o es inválido |
| 403 | El rol del usuario no tiene permiso para esa operación |
| 404 | No existe el registro con ese ID |
| 409 | RUT o correo de cliente ya registrado |

## Estructura del backend
```
tienda/src/main/java/sonidoVivo/tienda/
├── config/       Configuración (seguridad, CORS, Swagger, usuarios iniciales)
├── controller/   Endpoints REST (@RestController)
├── entity/       Entidades JPA (Producto, Cliente, Venta, Usuario)
├── exception/    Manejo global de errores
├── repository/   Repositorios (extienden JpaRepository)
├── security/     JWT (generación, validación y filtro)
└── service/      Lógica de negocio (@Service)
```

## Frontend (React)
> Pendiente: esta sección se completa cuando termine la integración con el frontend (cómo ejecutarlo con `npm start` en `http://localhost:3000` y qué pantallas incluye).