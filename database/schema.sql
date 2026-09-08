-- Base de Datos para el Sistema de Gestión Académica
-- Unidad Educativa "Dr. Alfredo Pareja Diezcanseco" (Ambato, Ecuador)
-- Motor: MySQL / MariaDB (InnoDB, utf8mb4)

CREATE DATABASE IF NOT EXISTS `gestion_academica_ueapd`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `gestion_academica_ueapd`;

SET FOREIGN_KEY_CHECKS = 0;

-- 1. Tabla de Institución
DROP TABLE IF EXISTS `institucion`;
CREATE TABLE `institucion` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `nombre` VARCHAR(150) NOT NULL,
  `codigo_amie` VARCHAR(20) NOT NULL UNIQUE,
  `direccion` VARCHAR(255) NOT NULL,
  `telefono` VARCHAR(20) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `ciudad` VARCHAR(50) DEFAULT 'Ambato',
  `provincia` VARCHAR(50) DEFAULT 'Tungurahua',
  `fecha_creacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Tabla de Usuarios
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `rol` ENUM('ADMINISTRADOR', 'PROFESOR', 'ALUMNO', 'REPRESENTANTE') NOT NULL,
  `nombres` VARCHAR(100) NOT NULL,
  `apellidos` VARCHAR(100) NOT NULL,
  `cedula` VARCHAR(10) NOT NULL UNIQUE,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `telefono` VARCHAR(20) NULL,
  `direccion` VARCHAR(255) NULL,
  `activo` TINYINT(1) DEFAULT 1,
  `debe_cambiar_pass` TINYINT(1) DEFAULT 0,
  `fecha_creacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_usuario_rol` (`rol`),
  INDEX `idx_usuario_cedula` (`cedula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Tabla de Períodos Lectivos
DROP TABLE IF EXISTS `periodos_lectivos`;
CREATE TABLE `periodos_lectivos` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `nombre` VARCHAR(50) NOT NULL, -- Ej: "2025-2026"
  `fecha_inicio` DATE NOT NULL,
  `fecha_fin` DATE NOT NULL,
  `activo` TINYINT(1) DEFAULT 1,
  `estado` ENUM('ABIERTO', 'CERRADO') DEFAULT 'ABIERTO',
  `fecha_creacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Tabla de Trimestres
DROP TABLE IF EXISTS `trimestres`;
CREATE TABLE `trimestres` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `periodo_id` INT NOT NULL,
  `numero` INT NOT NULL, -- 1, 2, 3
  `nombre` VARCHAR(50) NOT NULL, -- Ej: "Primer Trimestre"
  `fecha_inicio` DATE NOT NULL,
  `fecha_fin` DATE NOT NULL,
  `estado` ENUM('ABIERTO', 'CERRADO') DEFAULT 'ABIERTO',
  FOREIGN KEY (`periodo_id`) REFERENCES `periodos_lectivos` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `uk_periodo_trimestre` (`periodo_id`, `numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Tabla de Cursos (Grados/Años de Educación)
DROP TABLE IF EXISTS `cursos`;
CREATE TABLE `cursos` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `nombre` VARCHAR(100) NOT NULL, -- Ej: "8vo Año de Educación General Básica"
  `nivel` VARCHAR(50) NOT NULL -- Ej: "EGB Superior", "Bachillerato General Unificado"
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Tabla de Paralelos
DROP TABLE IF EXISTS `paralelos`;
CREATE TABLE `paralelos` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `curso_id` INT NOT NULL,
  `nombre` VARCHAR(10) NOT NULL, -- Ej: "A", "B"
  `periodo_id` INT NOT NULL,
  FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`periodo_id`) REFERENCES `periodos_lectivos` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `uk_curso_paralelo_periodo` (`curso_id`, `nombre`, `periodo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Tabla de Materias (Asignaturas)
DROP TABLE IF EXISTS `materias`;
CREATE TABLE `materias` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `nombre` VARCHAR(100) NOT NULL, -- Ej: "Matemáticas", "Lengua y Literatura"
  `codigo` VARCHAR(20) NOT NULL UNIQUE,
  `area` VARCHAR(100) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Tabla de Representantes
DROP TABLE IF EXISTS `representantes`;
CREATE TABLE `representantes` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `usuario_id` INT NOT NULL UNIQUE,
  `parentesco` VARCHAR(50) DEFAULT 'PADRE/MADRE/TUTOR',
  `ocupacion` VARCHAR(100) NULL,
  FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Tabla de Alumnos (Estudiantes)
DROP TABLE IF EXISTS `alumnos`;
CREATE TABLE `alumnos` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `usuario_id` INT NOT NULL UNIQUE,
  `codigo_estudiantil` VARCHAR(20) NOT NULL UNIQUE,
  `fecha_nacimiento` DATE NULL,
  `genero` ENUM('MASCULINO', 'FEMENINO', 'OTRO') DEFAULT 'MASCULINO',
  FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Tabla de Relación Representante - Alumno
DROP TABLE IF EXISTS `representante_alumno`;
CREATE TABLE `representante_alumno` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `representante_id` INT NOT NULL,
  `alumno_id` INT NOT NULL,
  `es_principal` TINYINT(1) DEFAULT 1,
  FOREIGN KEY (`representante_id`) REFERENCES `representantes` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `uk_rep_alumno` (`representante_id`, `alumno_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. Tabla de Profesores
DROP TABLE IF EXISTS `profesores`;
CREATE TABLE `profesores` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `usuario_id` INT NOT NULL UNIQUE,
  `especialidad` VARCHAR(100) NULL,
  `titulo` VARCHAR(100) NULL,
  FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. Tabla de Matrículas
DROP TABLE IF EXISTS `matriculas`;
CREATE TABLE `matriculas` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `alumno_id` INT NOT NULL,
  `paralelo_id` INT NOT NULL,
  `periodo_id` INT NOT NULL,
  `num_matricula` VARCHAR(30) NOT NULL UNIQUE,
  `fecha_matricula` DATE NOT NULL,
  `estado` ENUM('ACTIVA', 'RETIRO', 'GRADUADO') DEFAULT 'ACTIVA',
  FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`paralelo_id`) REFERENCES `paralelos` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`periodo_id`) REFERENCES `periodos_lectivos` (`id`) ON DELETE RESTRICT,
  UNIQUE KEY `uk_alumno_periodo` (`alumno_id`, `periodo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 13. Tabla de Asignaciones Docentes (Profesor - Materia - Paralelo)
DROP TABLE IF EXISTS `asignaciones_docentes`;
CREATE TABLE `asignaciones_docentes` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `profesor_id` INT NOT NULL,
  `materia_id` INT NOT NULL,
  `paralelo_id` INT NOT NULL,
  `periodo_id` INT NOT NULL,
  FOREIGN KEY (`profesor_id`) REFERENCES `profesores` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`materia_id`) REFERENCES `materias` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`paralelo_id`) REFERENCES `paralelos` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`periodo_id`) REFERENCES `periodos_lectivos` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `uk_profesor_materia_paralelo` (`profesor_id`, `materia_id`, `paralelo_id`, `periodo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 14. Tabla de Horarios de Clases
DROP TABLE IF EXISTS `horarios`;
CREATE TABLE `horarios` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `asignacion_id` INT NOT NULL,
  `dia_semana` ENUM('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES') NOT NULL,
  `hora_inicio` TIME NOT NULL,
  `hora_fin` TIME NOT NULL,
  `aula` VARCHAR(50) DEFAULT 'Aula Regular',
  FOREIGN KEY (`asignacion_id`) REFERENCES `asignaciones_docentes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 15. Tabla de Componentes de Evaluación (Configuración de Ponderaciones)
DROP TABLE IF EXISTS `componentes_evaluacion`;
CREATE TABLE `componentes_evaluacion` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `nombre` VARCHAR(50) NOT NULL, -- "Aportes", "Proyecto", "Evaluación"
  `codigo` VARCHAR(20) NOT NULL UNIQUE, -- "APORTES", "PROYECTO", "EVALUACION"
  `ponderacion_porcentaje` DECIMAL(5,2) NOT NULL, -- Ej: 40.00, 30.00, 30.00
  `descripcion` VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 16. Tabla de Calificaciones
DROP TABLE IF EXISTS `calificaciones`;
CREATE TABLE `calificaciones` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `alumno_id` INT NOT NULL,
  `asignacion_id` INT NOT NULL,
  `trimestre_id` INT NOT NULL,
  `componente_id` INT NOT NULL,
  `nota` DECIMAL(4,2) NULL, -- Escalado 0.00 a 10.00. NULL indica pendiente
  `observacion` VARCHAR(255) NULL,
  `fecha_registro` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `registrado_por` INT NOT NULL, -- usuario_id
  FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`asignacion_id`) REFERENCES `asignaciones_docentes` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`trimestre_id`) REFERENCES `trimestres` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`componente_id`) REFERENCES `componentes_evaluacion` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`registrado_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT,
  UNIQUE KEY `uk_calificacion_estudiante` (`alumno_id`, `asignacion_id`, `trimestre_id`, `componente_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 17. Tabla de Tareas (Deberes)
DROP TABLE IF EXISTS `tareas`;
CREATE TABLE `tareas` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `asignacion_id` INT NOT NULL,
  `trimestre_id` INT NOT NULL,
  `titulo` VARCHAR(150) NOT NULL,
  `descripcion` TEXT NOT NULL,
  `tipo` ENUM('DEBER', 'PROYECTO', 'INVESTIGACION', 'TALLER', 'OTRO') DEFAULT 'DEBER',
  `fecha_publicacion` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `fecha_limite` DATETIME NOT NULL,
  `archivo_adjunto` VARCHAR(255) NULL,
  FOREIGN KEY (`asignacion_id`) REFERENCES `asignaciones_docentes` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`trimestre_id`) REFERENCES `trimestres` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 18. Tabla de Entregas de Tareas
DROP TABLE IF EXISTS `entregas_tareas`;
CREATE TABLE `entregas_tareas` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `tarea_id` INT NOT NULL,
  `alumno_id` INT NOT NULL,
  `texto_entrega` TEXT NULL,
  `archivo_entrega` VARCHAR(255) NULL,
  `fecha_entrega` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `estado` ENUM('PENDIENTE', 'ENTREGADA', 'CALIFICADA', 'VENCIDA') DEFAULT 'ENTREGADA',
  `nota` DECIMAL(4,2) NULL,
  `retroalimentacion` TEXT NULL,
  `fecha_calificacion` DATETIME NULL,
  FOREIGN KEY (`tarea_id`) REFERENCES `tareas` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`) ON DELETE RESTRICT,
  UNIQUE KEY `uk_tarea_alumno` (`tarea_id`, `alumno_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 19. Tabla de Asistencia Diario Escolar
DROP TABLE IF EXISTS `asistencia`;
CREATE TABLE `asistencia` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `alumno_id` INT NOT NULL,
  `paralelo_id` INT NOT NULL,
  `trimestre_id` INT NOT NULL,
  `fecha` DATE NOT NULL,
  `estado` ENUM('PRESENTE', 'ATRASO', 'FALTA_INJUSTIFICADA', 'FALTA_JUSTIFICADA') NOT NULL,
  `observacion` VARCHAR(255) NULL,
  `registrado_por` INT NOT NULL,
  `version` INT DEFAULT 1,
  FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`paralelo_id`) REFERENCES `paralelos` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`trimestre_id`) REFERENCES `trimestres` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`registrado_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT,
  UNIQUE KEY `uk_asistencia_alumno_fecha` (`alumno_id`, `fecha`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 20. Tabla de Justificativos
DROP TABLE IF EXISTS `justificativos`;
CREATE TABLE `justificativos` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `asistencia_id` INT NOT NULL,
  `representante_id` INT NOT NULL,
  `motivo` TEXT NOT NULL,
  `archivo_evidencia` VARCHAR(255) NULL,
  `estado` ENUM('PENDIENTE', 'APROBADO', 'RECHAZADO') DEFAULT 'PENDIENTE',
  `observacion_admin` TEXT NULL,
  `atendido_por` INT NULL,
  `fecha_envio` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `fecha_respuesta` DATETIME NULL,
  FOREIGN KEY (`asistencia_id`) REFERENCES `asistencia` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`representante_id`) REFERENCES `representantes` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`atendido_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 21. Tabla de Seguimiento Disciplinario y Comportamiento
DROP TABLE IF EXISTS `comportamiento`;
CREATE TABLE `comportamiento` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `alumno_id` INT NOT NULL,
  `trimestre_id` INT NOT NULL,
  `calificacion_cualitativa` ENUM('A', 'B', 'C', 'D', 'E') DEFAULT 'A', -- A: Muy Satisfactorio, E: Insatisfactorio
  `observacion` TEXT NULL,
  `compromiso` TEXT NULL,
  `registrado_por` INT NOT NULL,
  `fecha_registro` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`trimestre_id`) REFERENCES `trimestres` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`registrado_por`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 22. Tabla de Comunicados Informativos
DROP TABLE IF EXISTS `comunicados`;
CREATE TABLE `comunicados` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `titulo` VARCHAR(150) NOT NULL,
  `contenido` TEXT NOT NULL,
  `remitente_id` INT NOT NULL,
  `dirigido_a_rol` ENUM('TODOS', 'ALUMNO', 'REPRESENTANTE', 'PROFESOR') DEFAULT 'TODOS',
  `paralelo_id` INT NULL, -- NULL si es para toda la institución
  `fecha_publicacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`remitente_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`paralelo_id`) REFERENCES `paralelos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 23. Tabla de Notificaciones
DROP TABLE IF EXISTS `notificaciones`;
CREATE TABLE `notificaciones` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `usuario_id` INT NOT NULL,
  `titulo` VARCHAR(150) NOT NULL,
  `mensaje` TEXT NOT NULL,
  `tipo` VARCHAR(50) DEFAULT 'INFO',
  `leido` TINYINT(1) DEFAULT 0,
  `fecha_creacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  INDEX `idx_notif_usuario_leido` (`usuario_id`, `leido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 24. Tabla de Tokens de Sesión y Rotación JWT
DROP TABLE IF EXISTS `user_sessions`;
CREATE TABLE `user_sessions` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `usuario_id` INT NOT NULL,
  `refresh_token_hash` VARCHAR(255) NOT NULL,
  `ip_address` VARCHAR(45) NULL,
  `user_agent` VARCHAR(255) NULL,
  `expira_en` DATETIME NOT NULL,
  `revocado` TINYINT(1) DEFAULT 0,
  `fecha_creacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 25. Tabla de Auditoría
DROP TABLE IF EXISTS `auditoria`;
CREATE TABLE `auditoria` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `usuario_id` INT NULL,
  `accion` VARCHAR(100) NOT NULL,
  `tabla_afectada` VARCHAR(100) NOT NULL,
  `registro_id` INT NULL,
  `detalles` JSON NULL,
  `ip` VARCHAR(45) NULL,
  `fecha` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
