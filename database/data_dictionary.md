# Diccionario de Datos - Base de Datos `gestion_academica_ueapd`

**Unidad Educativa Dr. Alfredo Pareja Diezcanseco (Ambato, Ecuador)**

---

## 1. Tabla: `institucion`
Almacena la información institucional de la Unidad Educativa.

| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | INT | NO | PRI | Identificador único autoincrementable. |
| `nombre` | VARCHAR(150) | NO | | Nombre oficial de la institución. |
| `codigo_amie` | VARCHAR(20) | NO | UNI | Código AMIE asignado por el Ministerio de Educación. |
| `direccion` | VARCHAR(255) | NO | | Dirección física. |
| `telefono` | VARCHAR(20) | NO | | Teléfono de contacto. |
| `email` | VARCHAR(100) | NO | | Correo electrónico oficial. |
| `ciudad` | VARCHAR(50) | SI | | Ciudad (Predeterminado: Ambato). |
| `provincia` | VARCHAR(50) | SI | | Provincia (Predeterminado: Tungurahua). |

---

## 2. Tabla: `usuarios`
Centraliza las credenciales de acceso y perfiles de los cuatro roles.

| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | INT | NO | PRI | Identificador único de usuario. |
| `username` | VARCHAR(50) | NO | UNI | Nombre de usuario para inicio de sesión. |
| `password_hash` | VARCHAR(255) | NO | | Hash seguro de contraseña (BCrypt). |
| `rol` | ENUM | NO | MUL | Rol del sistema (`ADMINISTRADOR`, `PROFESOR`, `ALUMNO`, `REPRESENTANTE`). |
| `nombres` | VARCHAR(100) | NO | | Nombres completos. |
| `apellidos` | VARCHAR(100) | NO | | Apellidos completos. |
| `cedula` | VARCHAR(10) | NO | UNI | Cédula de ciudadanía o identidad ecuatoriana. |
| `email` | VARCHAR(100) | NO | UNI | Correo electrónico personal/institucional. |
| `telefono` | VARCHAR(20) | SI | | Número celular o convencional. |
| `direccion` | VARCHAR(255) | SI | | Dirección domiciliaria. |
| `activo` | TINYINT(1) | NO | | Estado lógico del usuario (1 = Activo, 0 = Inactivo). |
| `debe_cambiar_pass` | TINYINT(1) | NO | | Flag para forzar cambio de clave temporal. |

---

## 3. Tabla: `calificaciones`
Registra las notas por componente, trimestre y materia.

| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | INT | NO | PRI | Identificador único. |
| `alumno_id` | INT | NO | MUL | Clave foránea a `alumnos.id`. |
| `asignacion_id` | INT | NO | MUL | Clave foránea a `asignaciones_docentes.id`. |
| `trimestre_id` | INT | NO | MUL | Clave foránea a `trimestres.id`. |
| `componente_id` | INT | NO | MUL | Clave foránea a `componentes_evaluacion.id`. |
| `nota` | DECIMAL(4,2) | SI | | Calificación numérica (0.00 a 10.00). `NULL` = Pendiente ("—"). |
| `observacion` | VARCHAR(255) | SI | | Comentario del profesor. |
| `registrado_por` | INT | NO | MUL | Clave foránea a `usuarios.id` (Docente/Admin). |

---

## 4. Tabla: `asistencia`
Registra la asistencia diaria escolar.

| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | INT | NO | PRI | Identificador único. |
| `alumno_id` | INT | NO | MUL | Clave foránea a `alumnos.id`. |
| `paralelo_id` | INT | NO | MUL | Clave foránea a `paralelos.id`. |
| `trimestre_id` | INT | NO | MUL | Clave foránea a `trimestres.id`. |
| `fecha` | DATE | NO | MUL | Fecha calendario del registro. |
| `estado` | ENUM | NO | | `PRESENTE`, `ATRASO`, `FALTA_INJUSTIFICADA`, `FALTA_JUSTIFICADA`. |
| `version` | INT | NO | | Control de concurrencia optimista. |

---

## 5. Tabla: `justificativos`
Gestión de solicitudes de justificación enviadas por representantes.

| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | INT | NO | PRI | Identificador de justificativo. |
| `asistencia_id` | INT | NO | MUL | Clave foránea a `asistencia.id`. |
| `representante_id` | INT | NO | MUL | Clave foránea a `representantes.id`. |
| `motivo` | TEXT | NO | | Explicación de la falta/atraso. |
| `archivo_evidencia` | VARCHAR(255) | SI | | Ruta relativa del archivo privado subido. |
| `estado` | ENUM | NO | | `PENDIENTE`, `APROBADO`, `RECHAZADO`. |
| `observacion_admin` | TEXT | SI | | Respuesta del administrador. |
