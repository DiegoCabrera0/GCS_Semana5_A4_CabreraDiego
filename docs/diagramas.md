# Diagramas de Arquitectura y Diseño
**Unidad Educativa Dr. Alfredo Pareja Diezcanseco**

---

## 1. Diagrama de Arquitectura de Capas

```mermaid
graph TD
    subgraph Cliente Android Native
        UI[Jetpack Compose UI & Material 3]
        VM[ViewModels & StateFlow]
        REPO[Repository Layer]
        ROOM[(Room Local Cache)]
    end

    subgraph XAMPP Apache Backend
        API[Front Controller index.php & Router]
        MIDDLEWARE[Auth & Role Middleware]
        CTRL[PHP Controllers]
        SERV[Business Services & Grade Calculator]
        DB_REPO[PHP Repositories & PDO]
    end

    subgraph Base de Datos Engine
        MYSQL[(MySQL / MariaDB InnoDB)]
    end

    UI --> VM
    VM --> REPO
    REPO --> ROOM
    REPO -- "HTTP / JSON REST (Retrofit & OkHttp)" --> API
    API --> MIDDLEWARE
    MIDDLEWARE --> CTRL
    CTRL --> SERV
    SERV --> DB_REPO
    DB_REPO -- "PDO Prepared Statements" --> MYSQL
```

---

## 2. Diagrama de Casos de Uso - Flujo de Justificativos

```mermaid
useCaseDiagram
    actor Representante as Rep
    actor Administrador as Admin
    
    usecase "Consultar Inasistencias" as UC1
    usecase "Enviar Justificativo con Evidencia" as UC2
    usecase "Revisar Justificativo Pendiente" as UC3
    usecase "Aprobar o Rechazar Justificativo" as UC4
    usecase "Actualizar Registro de Asistencia" as UC5
    
    Rep --> UC1
    Rep --> UC2
    Admin --> UC3
    Admin --> UC4
    UC4 ..> UC5 : <<includes>>
```

---

## 3. Diagrama de Secuencia - Proceso de Autenticación JWT

```mermaid
sequenceDiagram
    autonumber
    actor Usuario
    participant App as Android Client
    participant API as Web Service PHP
    participant DB as MySQL Database

    Usuario->>App: Ingresa Usuario y Contraseña
    App->>API: POST /auth/login {username, password}
    API->>DB: SELECT * FROM usuarios WHERE username = ?
    DB-->>API: Datos del usuario (password_hash)
    API->>API: password_verify(password, hash)
    API->>API: Generar JWT Token (HMAC-SHA256)
    API-->>App: 200 OK {token, usuario}
    App->>App: Guardar JWT en SharedPreferences / Keystore
    App-->>Usuario: Redirección al Panel según el Rol
```
