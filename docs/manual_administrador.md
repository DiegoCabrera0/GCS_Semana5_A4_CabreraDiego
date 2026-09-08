# Manual de Usuario - Rol ADMINISTRADOR
**Unidad Educativa Dr. Alfredo Pareja Diezcanseco**

## Credenciales de Demostración
* **Usuario:** `admin`
* **Contraseña:** `admin123`

---

## Funcionalidades Principales

### 1. Gestión de Usuarios
* **Acceso:** Menú Principal -> Módulo `Usuarios`.
* **Crear Usuario:** Presionar botón `+`, completar Nombres, Apellidos, Cédula (10 dígitos ecuatorianos), Usuario y Rol (`ADMINISTRADOR`, `PROFESOR`, `ALUMNO`, `REPRESENTANTE`).
* **Restablecer Contraseña:** Seleccionar el icono de candado en la tarjeta del usuario. Se genera una clave temporal forzando el cambio de clave en el siguiente inicio de sesión.
* **Desactivar Usuario:** Presionar el icono de basura. Aplica baja lógica para conservar el historial académico del estudiante sin eliminar registros históricos.

### 2. Aprobación y Procesamiento de Justificativos
* **Acceso:** Menú Principal -> Módulo `Justificativos`.
* **Visualizar Solicitudes:** Presenta la lista de justificaciones enviadas por los representantes con la fecha de falta, el alumno y la descripción del motivo.
* **Aprobar / Rechazar:** Presionar `Aprobar` o `Rechazar`. El servidor procesa la decisión dentro de una transacción en MySQL, actualizando automáticamente el registro de asistencia a `FALTA_JUSTIFICADA` y notificando al representante.

### 3. Publicación de Comunicados
* Permite crear avisos institucionales para toda la unidad educativa o dirigidos a roles específicos.
