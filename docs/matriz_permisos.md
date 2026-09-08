# Matriz de Control de Acceso Basado en Roles (RBAC)
**Unidad Educativa Dr. Alfredo Pareja Diezcanseco**

| Endpoint / Módulo | Método | ADMINISTRADOR | PROFESOR | ALUMNO | REPRESENTANTE |
|---|---|:---:|:---:|:---:|:---:|
| `/health` | GET |  Permitido |  Permitido |  Permitido |  Permitido |
| `/ready` | GET |  Permitido |  Permitido |  Permitido |  Permitido |
| `/auth/login` | POST |  Permitido |  Permitido |  Permitido |  Permitido |
| `/me` | GET |  Permitido |  Permitido |  Permitido |  Permitido |
| `/auth/change-password` | POST |  Permitido |  Permitido |  Permitido |  Permitido |
| `/users` | GET / POST / DELETE |  Permitido |  Denegado |  Denegado |  Denegado |
| `/users/{id}/reset-password` | POST |  Permitido |  Denegado |  Denegado |  Denegado |
| `/grades/student` | GET |  Permitido |  Denegado |  Permitido |  Permitido |
| `/grades/matrix` | GET |  Permitido |  Permitido |  Denegado |  Denegado |
| `/grades/update` | POST |  Permitido |  Permitido |  Denegado |  Denegado |
| `/tasks/student` | GET |  Denegado |  Denegado |  Permitido |  Permitido |
| `/tasks/teacher` | GET |  Denegado |  Permitido |  Denegado |  Denegado |
| `/tasks` (Crear) | POST |  Denegado |  Permitido |  Denegado |  Denegado |
| `/tasks/submit` (Entregar) | POST |  Denegado |  Denegado |  Permitido |  Denegado |
| `/attendance/student` | GET |  Permitido |  Denegado |  Permitido |  Permitido |
| `/attendance/mark` | POST |  Permitido |  Permitido |  Denegado |  Denegado |
| `/justifications` | GET |  Permitido |  Denegado |  Denegado |  Permitido |
| `/justifications/submit` | POST |  Denegado |  Denegado |  Denegado |  Permitido |
| `/justifications/process` | POST |  Permitido |  Denegado |  Denegado |  Denegado |
| `/announcements` | GET |  Permitido |  Permitido |  Permitido |  Permitido |
| `/announcements` (Publicar)| POST |  Permitido |  Permitido |  Denegado |  Denegado |
| `/notifications` | GET / POST |  Permitido |  Permitido |  Permitido |  Permitido |
