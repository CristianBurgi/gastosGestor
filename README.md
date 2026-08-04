# Gestor de Gastos Personales

Aplicación web para registrar y visualizar gastos personales mensuales,
reemplazando una planilla en papel. Desarrollada como proyecto personal
con Spring Boot + Thymeleaf + PostgreSQL.

---

## Stack tecnológico

| Capa         | Tecnología                     |
|--------------|-------------------------------|
| Backend      | Java 21, Spring Boot 3.3.x    |
| Persistencia | Spring Data JPA, Hibernate    |
| Base de datos| PostgreSQL                    |
| Migraciones  | Flyway                        |
| Frontend     | Thymeleaf (server-side HTML)  |
| Build        | Maven                         |

---

## Estructura del proyecto

El proyecto está organizado **por feature** (no por capa), igual que EventFoto:

```
src/main/java/com/cristian/gastos/
├── GastosPersonalesApplication.java   ← Entry point
├── categoria/                         ← Feature: Categorías de gasto (fase 1)
├── gasto/                             ← Feature: Gastos mensuales (fase 1)
└── (más features se agregan por fase)

src/main/resources/
├── db/migration/                      ← Scripts SQL de Flyway (Vx__nombre.sql)
├── templates/                         ← Plantillas Thymeleaf
└── application.properties             ← Config base
```

---

## Requisitos previos

- Java 21+
- Maven 3.9+
- PostgreSQL 15+ corriendo localmente

---

## Cómo levantar el proyecto en local

### 1. Crear la base de datos en PostgreSQL

```sql
CREATE DATABASE gastos_personales;
```

### 2. Configurar credenciales (si son distintas a las por defecto)

Crear o editar `src/main/resources/application-local.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gastos_personales
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

> ⚠️ Este archivo está en `.gitignore` para no exponer credenciales.

### 3. Levantar la aplicación

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

O desde IntelliJ: **Run Configuration → Active Profiles → local**

### 4. Verificar que levantó

Abrir en el navegador: [http://localhost:8080](http://localhost:8080)

---

## Variables de entorno / perfiles

| Perfil  | Descripción                              |
|---------|------------------------------------------|
| (base)  | `application.properties` — config común  |
| `local` | `application-local.properties` — DB local|

---

## Migraciones de base de datos

Las migraciones de Flyway se guardan en `src/main/resources/db/migration/`.
Se ejecutan automáticamente al arrancar la aplicación.

Convención de nombres: `V{version}__{descripcion_en_snake_case}.sql`

```
V1__crear_tabla_categorias.sql
V2__crear_tabla_gastos.sql
```

---

## Fases de desarrollo

- [x] **Fase 0** — Setup inicial (esqueleto del proyecto)
- [x] **Fase 1** — Entidades y migraciones (Categoría, Gasto, Ingreso)
- [x] **Fase 2** — CRUD de gastos e ingresos (API REST)
- [x] **Fase 3** — Frontend con Thymeleaf (Gastos e Ingresos)
- [x] **Fase 4** — Reportes y balance (Backend / API REST)
- [x] **Fase 5** — Vista Web Dashboard / Reportes (/reportes)
- [x] **Fase 6** — Gráficos de evolución con Chart.js (/reportes)
