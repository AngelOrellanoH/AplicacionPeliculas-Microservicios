# Plataforma Web de Películas – Arquitectura de Microservicios

Aplicación web desarrollada con **arquitectura de microservicios**, orientada a la gestión de películas, actores, usuarios y críticas.  
El sistema está construido sobre el ecosistema **Spring Boot y Spring Cloud**, incorporando **API Gateway, Eureka, seguridad JWT y roles**, y una interfaz web basada en **Spring MVC + Thymeleaf**.

Este proyecto fue desarrollado con el objetivo de aplicar buenas prácticas de diseño, desacoplamiento de servicios y comunicación entre microservicios.

---

## Arquitectura General

La aplicación está organizada en múltiples microservicios independientes que se comunican entre sí mediante REST y son descubiertos dinámicamente usando Eureka.

### Componentes principales:

- **Eureka Server**
  - Registro y descubrimiento de microservicios.
- **API Gateway (Spring Cloud Gateway)**
  - Punto de entrada único.
  - Enrutamiento de peticiones.
  - Validación de JWT y control de acceso.
- **Microservicio de Películas y Actores**
  - Gestión de películas, actores y sus relaciones.
- **Microservicio de Usuarios y Críticas**
  - Autenticación y autorización.
  - Gestión de usuarios, roles y críticas.
- **Frontend Web (Spring MVC + Thymeleaf)**
  - Interfaz para usuarios y administradores.
  - Consumo de servicios vía API Gateway.

---

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Cloud (Eureka, Gateway)**
- **Spring Security + JWT**
- **Spring Data JPA**
- **Thymeleaf**
- **MySQL**
- **Maven**
- **Postman** (pruebas)
- **Docker / Docker Compose** *(si aplica)*

---

## Seguridad y Autenticación

- Autenticación basada en **JWT (JSON Web Token)**.
- Tokens almacenados en **cookies HttpOnly**.
- Roles definidos:
  - `ROLE_USER`: usuario registrado.
  - `ROLE_ADMIN`: usuario administrador.
- Protección de rutas mediante filtros personalizados y `@PreAuthorize`.

---

## Funcionalidades Principales

### Usuarios
- Registro y autenticación
- Gestión de perfil
- Control de acceso por roles

### Películas y Actores
- CRUD completo de películas
- CRUD completo de actores
- Relación N:M entre películas y actores
- Búsqueda y filtrado

### Críticas
- Creación, edición y eliminación de críticas
- Visualización de críticas por película
- Promedio de valoraciones
- Control de propiedad (un usuario solo edita sus críticas)

### Panel de Administración
- Gestión de usuarios
- Gestión de películas
- Gestión de actores
- Gestión de críticas

---

## Estructura del Proyecto

```text
AplicacionPeliculas-Microservicios/
│
├── ApiGateWay/
├── EurekaServer/
├── ScriptsDataBase/
├── microservicio de peliculas/
├── microservicio de usuarios/
├── microservicio frontend/
└── README.md
