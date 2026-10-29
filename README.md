# Reto técnico – API de clientes

Microservicio reactivo para gestionar clientes, desarrollado con Java 21, Spring Boot 3.3.5, Spring WebFlux, Spring Data R2DBC y base de datos H2.

## Repositorio

Puedes clonar o descargar el proyecto directamente desde GitHub usando el siguiente enlace:

```bash
git clone https://github.com/adersonjara/reto-tecnico-pacifico-seguros.git
cd reto-tecnico-pacifico-seguros
```

## Requisitos

- Java 21 para ejecutar la aplicación localmente.
- Maven 3.6+ (o usar el wrapper incluido: `./mvnw`; en Windows, `mvnw.cmd`).
- Docker y Docker Compose para ejecutar el servicio en contenedor.

## Compilar y ejecutar localmente

Desde la raíz del proyecto:

```bash
./mvnw clean package
java -jar target/reto.tecnico-1.0.0-SNAPSHOT.jar
```

En Windows PowerShell o CMD, reemplaza `./mvnw` por `mvnw.cmd`.

La API estará disponible en `http://localhost:8080`.

## Ejecutar mediante Docker

El `Dockerfile` utiliza Java 21, la misma versión objetivo del proyecto.

1. Genera el JAR:

   ```bash
   ./mvnw clean package -DskipTests
   ```

2. Construye la imagen y levanta el contenedor:

   ```bash
   docker compose up --build -d
   ```

3. Revisa los logs:

   ```bash
   docker compose logs -f reto-tecnico
   ```

4. Detén el servicio:

   ```bash
   docker compose down
   ```

La aplicación se expone en `http://localhost:8080`. La base H2 está configurada como archivo local en `data/customerdb`; el archivo de datos puede persistir en la carpeta del proyecto cuando se ejecuta localmente. El contenedor Compose no declara un volumen específico para esa carpeta.

## Tests y cobertura

Ejecuta los tests y genera el reporte de cobertura JaCoCo con:

```bash
./mvnw clean verify
```

El comando ejecuta las pruebas y genera el reporte HTML de cobertura en:

```text
target/site/jacoco/index.html
```

Abre ese archivo en un navegador para revisar la cobertura total, por paquete y por clase. En Windows, utiliza `mvnw.cmd clean verify`.

## Endpoints

URL base: `http://localhost:8080`

| Método | Endpoint | Descripción | Respuesta esperada |
|---|---|---|---|
| `POST` | `/api/customers` | Crear un cliente | `201 Created` |
| `GET` | `/api/customers` | Listar clientes | `200 OK` |
| `GET` | `/api/customers/{id}` | Consultar un cliente | `200 OK` |
| `PUT` | `/api/customers/{id}` | Actualizar un cliente | `200 OK` |
| `DELETE` | `/api/customers/{id}` | Eliminar un cliente | `204 No Content` |

### Crear un cliente

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Aderson",
    "lastName": "Jara",
    "email": "aderson@gmail.com",
    "phone": "987654893"
  }'
```

`firstName` y `lastName` son obligatorios. Si se proporciona `email`, debe tener formato válido.

### Listar clientes

```bash
curl http://localhost:8080/api/customers
```

### Consultar un cliente por ID

```bash
curl http://localhost:8080/api/customers/1
```

### Actualizar un cliente

```bash
curl -X PUT http://localhost:8080/api/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Aderson",
    "lastName": "Jara Lazaro",
    "email": "adersonjara15@gmail.com",
    "phone": "987654893"
  }'
```

### Eliminar un cliente

```bash
curl -i -X DELETE http://localhost:8080/api/customers/1
```

## Documentación OpenAPI / Swagger

- OpenAPI JSON: `http://localhost:8080/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

La documentación interactiva permite revisar los contratos y probar los endpoints desde el navegador.
