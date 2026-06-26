# Sensibo Climate Control v2610 — API de Registro de Dispositivos

## Descripción general

API REST para el registro y gestión de dispositivos IoT de climatización (Sensibo). Construida con **Spring Boot 4.0.7 + Java 26 + PostgreSQL**, siguiendo **Domain-Driven Design (DDD)** y **Arquitectura Hexagonal**.

---

## Arquitectura: DDD + Hexagonal

### ¿Qué es DDD?

**Domain-Driven Design** es una metodología que pone el dominio del negocio en el centro. Se organiza en **Bounded Contexts** (contextos delimitados), cada uno con su propio modelo de dominio, independiente de los demás.

### ¿Qué es Hexagonal (Puertos y Adaptadores)?

Separa el núcleo de la aplicación (dominio + aplicación) de la infraestructura (bases de datos, APIs, etc.) mediante **puertos** (interfaces) y **adaptadores** (implementaciones). Esto permite cambiar la base de datos, el framework web, etc. sin tocar la lógica de negocio.

### Estructura de capas por Bounded Context

```
bounded-context/
├── domain/           ← Núcleo: entidades, value objects, repositorios (puertos)
├── application/      ← Casos de uso: comandos, queries, servicios
├── infrastructure/   ← Adaptadores: JPA, controladores REST, etc.
└── interfaces/       ← Controllers REST, DTOs (entrada/salida)
```

Cada **Bounded Context** replica esta estructura. Actualmente existe `registry/` (registro de dispositivos). Los componentes **compartidos** entre contextos van en `shared/`.

---

## Creación del proyecto paso a paso

### 1. Generar el proyecto en Spring Initializr

Abrir https://start.spring.io y configurar:

| Campo | Valor |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | 4.0.7 |
| Group | `com.sensibo.platform` |
| Artifact | `pc26010u202318609` |
| Package name | `com.sensibo.platform.pc26010u202318609` |
| Java | 26 |
| Packaging | Jar |

**Dependencias requeridas** (cada una se explica abajo):

- Spring Data JPA
- Spring Validation
- Spring Web MVC
- PostgreSQL Driver
- Lombok
- Spring Boot DevTools
- SpringDoc OpenAPI (Swagger)

> **¿Por qué estas dependencias?** Porque necesitamos: persistencia con JPA (`data-jpa`), base de datos real (`postgresql`), API REST (`webmvc`), validación de entrada (`validation`), documentación automática (`springdoc-openapi`), menos boilerplate (`lombok`) y recarga en desarrollo (`devtools`).

Descargar el ZIP, extraerlo y abrirlo en IntelliJ IDEA.

### 2. Configurar Java 26 en IntelliJ

`File → Project Structure → Project → SDK → 26`

Si no aparece, descargarlo desde `File → Project Structure → SDKs → + → Download JDK`.

### 3. Modificar `pom.xml`

**3.1. Agregar versión de Java** (Spring Initializr moderno ya la incluye):

```xml
<properties>
    <java.version>26</java.version>
</properties>
```

**3.2. Agregar dependencia `pluralize`** (para nombres de tablas en plural automático):

```xml
<dependency>
    <groupId>io.github.encryptorcode</groupId>
    <artifactId>pluralize</artifactId>
    <version>1.0.0</version>
</dependency>
```

> **¿Por qué `pluralize`?** La estrategia de naming que usamos convierte `Device` → `devices` (plural automático) para nombres de tablas. Esta librería hace esa conversión.

**3.3. Configurar Lombok como annotation processor** (en `<build><plugins>`):

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <executions>
        <execution>
            <id>default-compile</id>
            <phase>compile</phase>
            <goals><goal>compile</goal></goals>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </execution>
        <execution>
            <id>default-testCompile</id>
            <phase>test-compile</phase>
            <goals><goal>testCompile</goal></goals>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </execution>
    </executions>
</plugin>
```

> **¿Por qué?** Lombok necesita un **annotation processor** para generar getters, setters, constructores, etc. en tiempo de compilación. Sin esto, las anotaciones de Lombok no funcionan.

### 4. Configurar la clase principal

En `Pc26010u202318609Application.java` agregar `@EnableJpaAuditing`:

```java
@EnableJpaAuditing   // Habilita createdAt / updatedAt automáticos
@SpringBootApplication
public class Pc26010u202318609Application {
    public static void main(String[] args) {
        SpringApplication.run(Pc26010u202318609Application.class, args);
    }
}
```

> **¿Por qué `@EnableJpaAuditing`?** Activa el soporte de Spring Data JPA para llenar automáticamente los campos `@CreatedDate` y `@LastModifiedDate` en las entidades que extienden `AuditableAbstractPersistenceEntity`.

### 5. Crear la base de datos

Abrir pgAdmin, usar la contraseña `12345678` y crear una base de datos llamada `sensibo`.

### 6. Configurar `application.properties`

Reemplazar `src/main/resources/application.properties` con:

```properties
spring.application.name=pc26010u202318609

# --- Fuente de datos (PostgreSQL) ---
spring.datasource.url: jdbc:postgresql://localhost:5432/sensibo
spring.datasource.username: postgres
spring.datasource.password: 12345678
spring.datasource.driver-class-name: org.postgresql.Driver

# --- JPA / Hibernate ---
spring.jpa.database: postgresql
spring.jpa.show-sql: true
spring.jpa.hibernate.ddl-auto: update
spring.jpa.open-in-view=true
spring.jpa.properties.hibernate.format_sql: true
spring.jpa.properties.hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.naming.physical-strategy=com.sensibo.platform.pc26010u202318609.shared.infrastructure.persistence.jpa.configuration.strategy.SnakeCaseWithPluralizedTablePhysicalNamingStrategy

# --- Puerto del servidor ---
server.port: 8096

# --- Metadatos para OpenAPI ---
documentation.application.description=@project.description@
documentation.application.version=@project.version@

# --- Internacionalización (i18n) ---
spring.messages.basename=messages
spring.messages.encoding=UTF-8
```

> **¿Por qué cada línea?**
> - `ddl-auto: update`: Hibernate crea/actualiza las tablas automáticamente según las entidades. Para producción se usa `validate` o `none`.
> - `physical-strategy`: Convierte `Device` → `devices` (snake_case + plural), `serialNumber` → `serial_number`.
> - `open-in-view=true`: Mantiene la sesión de JPA abierta durante toda la petición HTTP (útil para lazy loading en controllers).
> - `server.port: 8096`: Puerto personalizado para evitar conflictos con otros proyectos.

---

## Estructura completa del proyecto

```
pc26010u202318609/
├── pom.xml
├── mvnw / mvnw.cmd                    # Maven Wrapper (build sin instalar Maven)
├── src/
│   ├── main/
│   │   ├── java/com/sensibo/platform/pc26010u202318609/
│   │   │   ├── Pc26010u202318609Application.java
│   │   │   ├── shared/                ← Componentes compartidos entre contextos
│   │   │   │   ├── domain/
│   │   │   │   │   └── model/aggregates/
│   │   │   │   │       └── AbstractDomainAggregateRoot.java
│   │   │   │   ├── application/
│   │   │   │   │   └── result/
│   │   │   │   │       ├── Result.java
│   │   │   │   │       └── ApplicationError.java
│   │   │   │   ├── infrastructure/
│   │   │   │   │   ├── persistence/jpa/
│   │   │   │   │   │   ├── entities/
│   │   │   │   │   │   │   └── AuditableAbstractPersistenceEntity.java
│   │   │   │   │   │   └── configuration/strategy/
│   │   │   │   │   │       └── SnakeCaseWithPluralizedTablePhysicalNamingStrategy.java
│   │   │   │   │   ├── i18n/configuration/
│   │   │   │   │   │   └── LocaleConfiguration.java
│   │   │   │   │   └── documentation/openapi/configuration/
│   │   │   │   │       └── OpenApiConfiguration.java
│   │   │   │   └── interfaces/rest/
│   │   │   │       ├── resources/
│   │   │   │       │   ├── ErrorResource.java
│   │   │   │       │   └── MessageResource.java
│   │   │   │       ├── transform/
│   │   │   │       │   ├── ErrorResponseAssembler.java
│   │   │   │       │   └── ResponseEntityAssembler.java
│   │   │   │       └── GlobalExceptionHandler.java
│   │   │   └── registry/              ← Bounded Context: Registro de dispositivos
│   │   │       ├── domain/
│   │   │       │   ├── model/
│   │   │       │   │   ├── valueobjects/
│   │   │       │   │   │   ├── DeviceTypes.java
│   │   │       │   │   │   ├── MacAddress.java
│   │   │       │   │   │   ├── SensiboUserId.java
│   │   │       │   │   │   ├── SerialNumber.java
│   │   │       │   │   │   └── Version.java
│   │   │       │   │   ├── commands/
│   │   │       │   │   │   └── CreateDeviceCommand.java
│   │   │       │   │   ├── queries/
│   │   │       │   │   │   └── GetDeviceById.java
│   │   │       │   │   └── aggregates/
│   │   │       │   │       └── Device.java
│   │   │       │   └── repositories/
│   │   │       │       └── DeviceRepository.java
│   │   │       └── infrastructure/
│   │   │           └── persistence/jpa/converters/
│   │   │               └── MacAddressPersistenceConverter.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/java/.../
│       └── Pc26010u202318609ApplicationTests.java
└── .gitignore, .gitattributes, etc.
```

---

## Explicación detallada de cada componente

### Paquete `shared/` — Infraestructura compartida

Contiene código reusable por **todos los Bounded Contexts**, manteniendo las dependencias técnicas fuera del dominio.

#### `shared/domain/model/aggregates/AbstractDomainAggregateRoot.java`

```java
public abstract class AbstractDomainAggregateRoot<T extends AbstractDomainAggregateRoot<T>>
        extends AbstractAggregateRoot<T> { ... }
```

**¿Qué es?** Clase base para todos los **Agregados Raíz** del dominio. Extiende `AbstractAggregateRoot` de Spring Data Commons.

**¿Por qué?** Proporciona:
- **Registro de eventos de dominio** (`registerDomainEvent`): permite que los agregados emitan eventos que se publicarán automáticamente después de persistir.
- Sin dependencias JPA: el dominio permanece limpio de infraestructura.
- **`domainEvents()` y `clearDomainEvents()`** expuestos como `public` para que los adaptadores de repositorio puedan publicar y limpiar eventos.

---

#### `shared/application/result/Result.java`

```java
public sealed interface Result<T, E> {
    record Success<T, E>(T value) implements Result<T, E> {}
    record Failure<T, E>(E error) implements Result<T, E> {}
    // ... map, flatMap, recover, etc.
}
```

**¿Qué es?** Un **Mónada Result** (tipo `Either` o `Result` de lenguajes funcionales). Usa `sealed interface` de Java para restringir los casos a `Success` o `Failure`.

**¿Por qué?** En lugar de lanzar excepciones para errores esperados (como "dispositivo no encontrado"), devolvemos un `Result.Failure` con un `ApplicationError`. Esto:
- Obliga a quien llama a manejar ambos casos (success/failure).
- Evita excepciones como flujo de control.
- Funciona muy bien con el patrón de **casos de uso** en DDD.

**Métodos clave:**
- `map()`: transforma el valor si es `Success`.
- `flatMap()`: encadena operaciones que devuelven `Result`.
- `recover()`: recupera de un error.
- `mapError()`: transforma el error.
- `getOrElse()`: valor por defecto si es `Failure`.

---

#### `shared/application/result/ApplicationError.java`

```java
public record ApplicationError(String code, String message, String details) { ... }
```

**¿Qué es?** Representación estandarizada de un error de aplicación.

**¿Por qué?** Centraliza los tipos de error:
- `validationError` → 400 Bad Request
- `notFound` → 404 Not Found
- `businessRuleViolation` → 422 Unprocessable Entity
- `conflict` → 409 Conflict
- `unexpected` → 500 Internal Server Error

Cada `code` se mapea a un HTTP status en `ErrorResponseAssembler` y a un mensaje internacionalizado en `messages.properties`.

---

#### `shared/infrastructure/persistence/jpa/entities/AuditableAbstractPersistenceEntity.java`

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableAbstractPersistenceEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
```

**¿Qué es?** Clase base para todas las **entidades JPA** que requieren auditoría.

**¿Por qué?**
- **`@MappedSuperclass`**: las entidades concretas heredan los campos `id`, `createdAt`, `updatedAt`.
- **`@EntityListeners(AuditingEntityListener.class)`**: Spring Data JPA llena `createdAt` al insertar y `updatedAt` al actualizar (requiere `@EnableJpaAuditing`).
- **`GenerationType.IDENTITY`**: usa `SERIAL` o `IDENTITY` de PostgreSQL para auto-increment.
- **`setId(Long)`**: necesario para que los **assemblers** de infraestructura puedan asignar el ID al reconstruir desde la BD.

> **¿Por qué separar `AuditableAbstractPersistenceEntity` de `AbstractDomainAggregateRoot`?** Porque pertenecen a capas distintas. El agregado raíz vive en el **dominio** (sin JPA), mientras que la entidad de persistencia vive en la **infraestructura** (con JPA). Esto mantiene el dominio puro y testeable sin base de datos.

---

#### `shared/infrastructure/persistence/jpa/configuration/strategy/SnakeCaseWithPluralizedTablePhysicalNamingStrategy.java`

**¿Qué hace?** Implementa `PhysicalNamingStrategy` de Hibernate para:
1. Convertir **CamelCase a snake_case**: `serialNumber` → `serial_number`
2. Pluralizar nombres de tabla: `Device` → `devices`

**¿Por qué?** Convención en bases de datos PostgreSQL: tablas en snake_case y plural (`devices`), columnas en snake_case y singular (`serial_number`, `mac_address`).

---

#### `shared/infrastructure/i18n/configuration/LocaleConfiguration.java`

**¿Qué hace?** Configura `AcceptHeaderLocaleResolver` para leer el locale del header `Accept-Language`.

**¿Por qué?** Soporta **inglés** y **español** (`es`). Los mensajes de error se traducen según el locale del cliente. Los archivos de traducción van en `src/main/resources/messages.properties` y `messages_es.properties`.

---

#### `shared/infrastructure/documentation/openapi/configuration/OpenApiConfiguration.java`

**¿Qué hace?** Configura **SpringDoc OpenAPI** (Swagger UI) con la información del proyecto, servidores (local, staging, producción) y datos de contacto.

**¿Por qué?** Genera automáticamente la documentación de la API en `/swagger-ui.html`. Los valores como `application.description` se toman de `application.properties` (y estos del `pom.xml` via `@project.description@`).

---

#### `shared/interfaces/rest/resources/ErrorResource.java` y `MessageResource.java`

**¿Qué son?** DTOs (Data Transfer Objects) para respuestas HTTP.
- `ErrorResource`: `{ "code": "...", "message": "...", "details": "..." }`
- `MessageResource`: `{ "message": "..." }`

**¿Por qué records?** Los `record` de Java son inmutables, concisos y ya incluyen `equals()`, `hashCode()`, `toString()` y constructor. Perfectos para DTOs.

---

#### `shared/interfaces/rest/transform/ErrorResponseAssembler.java`

**¿Qué hace?** Convierte `ApplicationError` → `ResponseEntity<ErrorResource>`.

**Lógica:**
1. Mapea el `error.code` a un HTTP status (`VALIDATION_ERROR` → 400, `*_NOT_FOUND` → 404, etc.).
2. Busca un mensaje internacionalizado en `messages.properties` usando claves como `error.validation.message`.
3. Si no hay traducción, usa el mensaje por defecto del `ApplicationError`.

---

#### `shared/interfaces/rest/transform/ResponseEntityAssembler.java`

**¿Qué hace?** Convierte `Result<T, ApplicationError>` → `ResponseEntity<?>`:
- `Success` → `200/201` con el recurso.
- `Failure` → delega en `ErrorResponseAssembler`.

**¿Por qué?** Unifica la creación de respuestas HTTP en toda la aplicación.

---

#### `shared/interfaces/rest/GlobalExceptionHandler.java`

**¿Qué hace?** `@RestControllerAdvice` global que captura excepciones no manejadas:
- `MethodArgumentNotValidException` → errores de validación (400).
- `IllegalArgumentException` → argumentos inválidos (400).
- `RuntimeException` / `Exception` → error inesperado (500).

**¿Por qué?** Centraliza el manejo de errores para que ningún error no capturado llegue al cliente sin formato.

---

### Paquete `registry/` — Bounded Context de Registro de Dispositivos

Este contexto se encarga del **registro y consulta de dispositivos Sensibo**.

#### Value Objects

Los **Value Objects** son objetos inmutables que representan conceptos del dominio sin identidad propia. Se comparan por valor, no por referencia.

**¿Por qué usarlos?** En lugar de usar `String` o `Long` directamente, creamos tipos específicos que:
- **Encapsulan validación**: un `MacAddress` siempre tiene formato válido.
- **Auto-documentan el dominio**: `SensiboUserId` no es cualquier `Long`, es el ID de un usuario Sensibo.
- **Son inmutables**: no pueden cambiar después de creados.

##### `DeviceTypes` (enum)

```java
public enum DeviceTypes {
    SMART_AC_CONTROLLER(1),
    ROOM_SENSOR(2),
    AIR_QUALITY_MONITOR(3),
    DOOR_WINDOW_SENSOR(4);
}
```

**¿Por qué enum?** Los tipos de dispositivo son un conjunto fijo y cerrado. El `id` numérico permite persistir como entero mientras se usa el nombre en código.

---

##### `MacAddress`

```java
public record MacAddress(String value) {
    public MacAddress {
        if (!value.matches("^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$"))
            throw new IllegalArgumentException("...");
    }
}
```

**¿Por qué?** Una dirección MAC tiene un formato específico (`AA:BB:CC:DD:EE:FF`). Al crear un `MacAddress`, se valida automáticamente. Nunca puede existir una dirección MAC inválida en el sistema.

---

##### `SensiboUserId`

**¿Por qué?** El ID de usuario debe ser un número positivo. Al encapsularlo, garantizamos que cualquier `SensiboUserId` válido tiene un `userId > 0`.

---

##### `SerialNumber`

**¿Por qué?** El número de serie es un UUID en formato estándar (36 caracteres). El constructor sin argumentos genera uno aleatorio: `new SerialNumber()`.

---

##### `Version`

**¿Por qué?** La versión de firmware sigue el formato semántico `X.Y.Z`. Validamos con una regex para evitar inconsistencias.

---

#### Commands y Queries

##### `CreateDeviceCommand`

```java
public record CreateDeviceCommand(
    Long userId, String deviceType, String modelName,
    String serialNumber, String macAddress,
    String firmwareVersion, LocalDate installationDate,
    String roomLocation
) { ... }
```

**¿Qué es?** Un **Command** (patrón CQRS) que encapsula la intención de crear un dispositivo. Contiene datos planos (sin value objects) porque viene del exterior (JSON request).

**¿Por qué?** Separa la **entrada** (datos crudos del request) del **modelo de dominio** (transformado a value objects dentro del servicio de aplicación).

---

##### `GetDeviceById`

```java
public record GetDeviceById(Long deviceId) { ... }
```

**¿Qué es?** Una **Query** que representa "buscar dispositivo por ID".

**¿Por qué?** Al igual que los commands, las queries son objetos explícitos que documentan las consultas disponibles.

---

#### Agregado Raíz: `Device`

```java
@Getter
@Setter
public class Device extends AbstractDomainAggregateRoot { ... }
```

**¿Qué es?** El **Agregado Raíz** del contexto `registry`. Es la entidad principal a través de la cual se accede y modifica el estado.

**¿Por qué `@Setter` en algunos campos y no en otros?**
- `id`: `@Setter` para que la infraestructura pueda asignarlo (solo el repositorio debe hacerlo).
- `modelName`, `installationDate`, `roomLocation`: Setters públicos porque son modificables (ej: cambiar la ubicación de la sala).
- `userId`, `deviceType`, `serialNumber`, `macAddress`, `firmwareVersion`: **Solo lectura** una vez creados, porque identifican al dispositivo de forma única.

**¿Por qué extiende `AbstractDomainAggregateRoot` y no `AuditableAbstractPersistenceEntity`?** Porque `Device` es un **objeto de dominio**, no una entidad JPA. La persistencia se maneja por separado en la capa de infraestructura.

---

#### Repositorio: `DeviceRepository`

```java
public interface DeviceRepository {
    Device save(Device device);
    Optional<Device> findById(Long id);
    boolean existsBySerialNumberAndMacAddress(String serialNumber, String macAddress);
}
```

**¿Qué es?** Un **Puerto** (interfaz) en términos de hexagonal. Define las operaciones de persistencia que el dominio necesita.

**¿Por qué interfaz?** La implementación concreta (JPA, MongoDB, etc.) se inyecta en tiempo de ejecución. El dominio no sabe ni le importa cómo se persisten los datos.

---

#### Converter: `MacAddressPersistenceConverter`

```java
@Converter
public class MacAddressPersistenceConverter { }
```

**¿Qué debería hacer?** Implementar `AttributeConverter<MacAddress, String>` para que JPA convierta automáticamente `MacAddress` ↔ `String` al persistir.

**¿Por qué existe?** Sin este converter, JPA no sabría cómo mapear el record `MacAddress` a una columna VARCHAR.

---

## Cómo agregar un nuevo Bounded Context

Supongamos que queremos crear el contexto `inventory` (gestión de inventario):

1. Copiar la plantilla de `bounded.zip` o replicar manualmente la estructura:

```
registry/  →  inventory/
├── domain/
│   ├── model/
│   │   ├── valueobjects/
│   │   ├── commands/
│   │   ├── queries/
│   │   └── aggregates/
│   └── repositories/
├── application/
│   └── internal/
│       └── commandservices/
│       └── queryservices/
├── infrastructure/
│   └── persistence/jpa/
│       ├── entities/
│       ├── repositories/
│       └── converters/
└── interfaces/
    └── rest/
        ├── resources/
        ├── transform/
        └── controllers/
```

2. Definir los **Value Objects** del dominio.
3. Definir los **Commands y Queries**.
4. Crear el **Agregado Raíz** extendiendo `AbstractDomainAggregateRoot`.
5. Definir la interfaz del **Repositorio**.
6. Implementar el **servicio de aplicación** (command handler).
7. Crear la **entidad JPA** extendiendo `AuditableAbstractPersistenceEntity`.
8. Crear el **adaptador JPA** del repositorio.
9. Crear el **controlador REST**.
10. Crear los **Assemblers** (DTO ↔ dominio) en `interfaces/rest/transform/`.

---

## Comandos útiles

```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Ejecutar la aplicación
./mvnw spring-boot:run

# Swagger UI (una vez ejecutando)
# http://localhost:8096/swagger-ui.html
```

---

## Notas importantes

- El `GlobalExceptionHandler.java` debe importar `com.sensibo.platform.pc26010u202318609.shared.*` (NO `u202610123`).
- `MacAddressPersistenceConverter` necesita implementar `AttributeConverter<MacAddress, String>` para funcionar.
- Se requiere PostgreSQL corriendo en `localhost:5432` con la base de datos `sensibo`.
- Contraseña de PostgreSQL: `12345678` (configurable en `application.properties`).
