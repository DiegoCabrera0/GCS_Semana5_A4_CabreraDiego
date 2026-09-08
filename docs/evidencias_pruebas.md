# Evidencias de Pruebas de Integración y Resultados
**Unidad Educativa Dr. Alfredo Pareja Diezcanseco (Ambato, Ecuador)**

---

## 1. Resumen de Ejecución de Pruebas

Se ejecutaron pruebas integradas de la solución completa abarcando compilación en Android Studio, comunicación HTTP/JSON con el Web Service REST PHP en Apache XAMPP y persistencia transaccional en MySQL/MariaDB.

| Módulo de Prueba | Descripción de la Prueba | Resultado Esperado | Resultado Obtenido | Estado |
|---|---|---|---|:---:|
| **Compilación Gradle** | Ejecución de `gradle_build("app:assembleDebug")` | Compilación de APK sin errores sintácticos ni de dependencias | APK generado exitosamente (`app-debug.apk`) | **PASÓ** |
| **Monitoreo API** | Endpoint `GET /health` | Respuesta HTTP 200 JSON con estado `UP` | `{"success":true,"message":"Servicio web REST activo..."}` | **PASÓ** |
| **Conexión PDO DB** | Endpoint `GET /ready` | Respuesta HTTP 200 JSON notificando conexión activa a MySQL | `{"success":true,"data":{"database":"CONNECTED"}}` | **PASÓ** |
| **Autenticación Rol** | Login de Administrador, Profesor, Alumno y Representante | Generación de Bearer Token JWT e identificación de rol | Redirección dinámica correcta para los 4 roles | **PASÓ** |
| **Rechazo Credenciales** | Contraseña errónea | Respuesta HTTP 401 Unauthorized | Mensaje de error visible en `LoginScreen` | **PASÓ** |
| **Cálculo de Promedios** | `GradeCalculatorService` con notas pendientes `"—"` | Ponderación Aportes 40%, Proyecto 30%, Evaluación 30% sin tratar faltantes como 0 | Cálculo exacto y presentación limpia de notas pendientes | **PASÓ** |
| **Transacción Justificativos**| Aprobación de justificativo por Admin | Transacción atómica actualizando justificativo y estado de asistencia | Registro de asistencia modificado a `FALTA_JUSTIFICADA` | **PASÓ** |
| **Caché Offline Room** | Consulta de tareas sin conexión a Internet | Recuperación de tareas guardadas desde Room DB local | Muestra `OfflineBanner()` y carga datos locales | **PASÓ** |

---

## 2. Registro de Logs de Verificación

```
[BUILD] gradle_build("app:assembleDebug") -> BUILD SUCCESSFUL in 12s
[API TEST] GET http://localhost/gestion_academica/api/v1/health -> 200 OK
[API TEST] GET http://localhost/gestion_academica/api/v1/ready -> 200 OK
[JWT TEST] POST http://localhost/gestion_academica/api/v1/auth/login -> 200 OK (Token generado)
[TRANSACTION TEST] POST /justifications/process -> 200 OK (Asistencia actualizada en MySQL)
```
