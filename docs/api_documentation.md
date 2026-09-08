# Especificación de la API REST - OpenAPI v1
**Unidad Educativa Dr. Alfredo Pareja Diezcanseco**

**Base URL:** `http://localhost/gestion_academica/api/v1`

---

## Endpoints de la API

### 1. Autenticación

#### `POST /auth/login`
* **Público:** Sí
* **Cuerpo:**
  ```json
  { "username": "admin", "password": "admin123" }
  ```
* **Respuesta 200 OK:**
  ```json
  {
    "success": true,
    "message": "Inicio de sesión exitoso",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "usuario": {
        "id": 1,
        "username": "admin",
        "rol": "ADMINISTRADOR",
        "nombres": "Carlos Eduardo",
        "apellidos": "Mendoza Ramos",
        "email": "admin@ueapd.edu.ec",
        "cedula": "1803456789",
        "debe_cambiar_pass": false
      }
    }
  }
  ```

#### `GET /me`
* **Autenticado:** Sí (Cualquier rol)
* **Headers:** `Authorization: Bearer <token>`
* **Respuesta 200 OK:** Perfil del usuario autenticado.

---

### 2. Calificaciones

#### `GET /grades/student?alumno_id={id}`
* **Roles Autorizados:** `ALUMNO`, `REPRESENTANTE`
* **Respuesta 200 OK:**
  ```json
  {
    "success": true,
    "data": {
      "estudiante": { "alumno_id": 1, "nombres": "Mateo", "apellidos": "Mendoza" },
      "materias": [
        {
          "materia_id": 1,
          "materia": "Matemáticas",
          "profesor": "Patricio Alvarez",
          "trimestres": {
            "1": { "aportes": 9.50, "proyecto": 9.00, "evaluacion": 8.50, "promedio": 9.05 },
            "2": { "aportes": 9.00, "proyecto": 8.80, "evaluacion": null, "promedio": 8.91 }
          },
          "promedio_final": 8.98
        }
      ]
    }
  }
  ```

#### `POST /grades/update`
* **Roles Autorizados:** `PROFESOR`, `ADMINISTRADOR`
* **Cuerpo:**
  ```json
  {
    "alumno_id": 1,
    "asignacion_id": 1,
    "trimestre_id": 2,
    "componente_id": 1,
    "nota": 9.50,
    "observacion": "Excelente trabajo"
  }
  ```

---

### 3. Justificativos

#### `POST /justifications/submit`
* **Roles Autorizados:** `REPRESENTANTE`
* **Form-Data:** `asistencia_id`, `motivo`, `evidencia` (archivo PDF/JPG).

#### `POST /justifications/process`
* **Roles Autorizados:** `ADMINISTRADOR`
* **Cuerpo:**
  ```json
  {
    "justificativo_id": 1,
    "estado": "APROBADO",
    "observacion": "Certificado médico válido."
  }
  ```
