# Matriz de Trazabilidad de Requisitos
**Unidad Educativa Dr. Alfredo Pareja Diezcanseco**

| Requisito del Sistema | Pantalla Móvil Android | Endpoint API REST | Tabla Base de Datos | Estado Prueba |
|---|---|---|---|:---:|
| Autenticación con 4 Roles | `LoginScreen.kt` | `POST /auth/login` | `usuarios` |  Aprobado |
| Cambio de Contraseña Seguro | `ChangePasswordScreen.kt` | `POST /auth/change-password` | `usuarios` |  Aprobado |
| CRUD Usuarios y Reset Claves | `AdminUsersScreen.kt` | `GET/POST/DELETE /users` | `usuarios` |  Aprobado |
| Libreta Notas y Promedio | `StudentGradesScreen.kt` | `GET /grades/student` | `calificaciones` |  Aprobado |
| Matriz Registro Notas Profesor| `TeacherGradesScreen.kt` | `POST /grades/update` | `calificaciones` |  Aprobado |
| Consulta Tareas y Entregas | `StudentTasksScreen.kt` | `GET/POST /tasks` | `tareas`, `entregas_tareas` |  Aprobado |
| Visualización de Asistencia | `StudentAttendanceScreen.kt` | `GET /attendance/student` | `asistencia` |  Aprobado |
| Justificativos y Proceso Admin | `ParentJustificationsScreen.kt`, `AdminJustificationsScreen.kt` | `POST /justifications/submit`, `/process` | `justificativos`, `asistencia` |  Aprobado |
| Comunicados Institucionales | `StudentDashboardScreen.kt` | `GET /announcements` | `comunicados` |  Aprobado |
| Modo Caché Offline Room | `OfflineBanner()` | N/A (Room DB Local) | `cached_tasks`, `cached_grades` |  Aprobado |
