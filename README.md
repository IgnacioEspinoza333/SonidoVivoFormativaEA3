# Sonido Vivo - Backend (Semana 1)

Proyecto académico en **Spring Boot** con **JPA** y **MySQL**.
Expone un CRUD completo para tres entidades independientes: `Producto`, `Cliente` y `Venta`.

## Requisitos
- Java 17
- MySQL (XAMPP) encendido en el puerto 3306
- No hace falta instalar Maven: el proyecto incluye Maven Wrapper (`mvnw`)

## Cómo ejecutar
1. Encender **MySQL** en XAMPP. La base `tienda_sonido_vivo` se crea sola si no existe.
2. Abrir una terminal dentro de la carpeta `tienda`.
3. Ejecutar:
   ```
   .\mvnw.cmd spring-boot:run
   ```
   (en Linux o Mac: `./mvnw spring-boot:run`)
4. La API queda disponible en `http://localhost:8080`.

## Estructura del proyecto
```
tienda/src/main/java/sonidoVivo/tienda/
├── entity/       Entidades JPA (Producto, Cliente, Venta)
├── repository/   Repositorios (extienden JpaRepository)
├── service/      Lógica de negocio (@Service)
└── controller/   Endpoints REST (@RestController)
```

## Cómo se exponen los 5 endpoints CRUD
Cada controlador usa `@RestController` y `@RequestMapping` con una ruta base.
Dentro de él, cada operación del servicio se expone con una anotación distinta:

| Operación | Anotación | Ruta | Respuesta |
|---|---|---|---|
| Obtener todos | `@GetMapping` | `/api/{recurso}` | 200 con la lista |
| Buscar por ID | `@GetMapping("/{id}")` | `/api/{recurso}/{id}` | 200, o 404 si no existe |
| Crear | `@PostMapping` | `/api/{recurso}` | 201 con el registro creado |
| Actualizar | `@PutMapping("/{id}")` | `/api/{recurso}/{id}` | 200, o 404 si no existe |
| Eliminar | `@DeleteMapping("/{id}")` | `/api/{recurso}/{id}` | 204, o 404 si no existe |

## Rutas por entidad
| Entidad | Ruta base |
|---|---|
| Producto | `/api/productos` |
| Cliente | `/api/clientes` |
| Venta | `/api/ventas` |

## Ejemplos de JSON

**Producto**
```json
{
  "nombre": "Guitarra electrica",
  "categoria": "Cuerdas",
  "marca": "Fender",
  "precio": 250000,
  "stock": 5,
  "descripcion": "Guitarra de prueba",
  "imagen": "guitarra.jpg"
}
```

**Cliente**
```json
{
  "nombre": "Juan",