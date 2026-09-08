-- Script de Datos de Demostración (Seed Data)
-- Unidad Educativa "Dr. Alfredo Pareja Diezcanseco" (Ambato, Ecuador)
-- Contraseñas iniciales válidas generadas con PHP password_hash():
-- Admin (admin): 'admin' o 'admin123'
-- Profesores: 'profesor123'
-- Alumnos: 'alumno123'
-- Representantes: 'rep123'

USE `gestion_academica_ueapd`;

SET FOREIGN_KEY_CHECKS = 0;

-- 1. Institución
TRUNCATE TABLE `institucion`;
INSERT INTO `institucion` (`id`, `nombre`, `codigo_amie`, `direccion`, `telefono`, `email`, `ciudad`, `provincia`) VALUES
(1, 'Unidad Educativa Dr. Alfredo Pareja Diezcanseco', '18H00123', 'Av. Los Capulíes y Los Guayabos, Ambato', '032840123', 'info@ueapd.edu.ec', 'Ambato', 'Tungurahua');

-- 2. Usuarios
TRUNCATE TABLE `usuarios`;

-- Admin (ID 1) -> Contraseña: 'admin'
INSERT INTO `usuarios` (`id`, `username`, `password_hash`, `rol`, `nombres`, `apellidos`, `cedula`, `email`, `telefono`, `direccion`, `activo`, `debe_cambiar_pass`) VALUES
(1, 'admin', '$2y$10$coPunMSkW4.GnBK2uWZcf..3AnlkfgOQEz.OE7l4I492WsUfsMH/2', 'ADMINISTRADOR', 'Carlos Eduardo', 'Mendoza Ramos', '1803456789', 'admin@ueapd.edu.ec', '0991234567', 'Ambato Centro', 1, 0);

-- Profesores (ID 2 al 7) -> Contraseña: 'profesor123'
INSERT INTO `usuarios` (`id`, `username`, `password_hash`, `rol`, `nombres`, `apellidos`, `cedula`, `email`, `telefono`, `direccion`, `activo`, `debe_cambiar_pass`) VALUES
(2, 'prof_matematicas', '$2y$10$zP2QGvl598XPWZJYpSrLY.Mw2ZwL45ofFsDsBWlcTAsXve5PIZckS', 'PROFESOR', 'Gonzalo Patricio', 'Alvarez Castro', '1801112223', 'galvarez@ueapd.edu.ec', '0992223334', 'Ficoa, Ambato', 1, 0),
(3, 'prof_lengua', '$2y$10$zP2QGvl598XPWZJYpSrLY.Mw2ZwL45ofFsDsBWlcTAsXve5PIZckS', 'PROFESOR', 'Maria Elena', 'Salazar Lopez', '1802223334', 'msalazar@ueapd.edu.ec', '0993334445', 'Atocha, Ambato', 1, 0),
(4, 'prof_ciencias', '$2y$10$zP2QGvl598XPWZJYpSrLY.Mw2ZwL45ofFsDsBWlcTAsXve5PIZckS', 'PROFESOR', 'Jorge Luis', 'Benitez Torres', '1803334445', 'jbenitez@ueapd.edu.ec', '0994445556', 'Huachi Chico, Ambato', 1, 0),
(5, 'prof_sociales', '$2y$10$zP2QGvl598XPWZJYpSrLY.Mw2ZwL45ofFsDsBWlcTAsXve5PIZckS', 'PROFESOR', 'Ana Lucía', 'Morales Vega', '1804445556', 'amorales@ueapd.edu.ec', '0995556667', 'Ingahurco, Ambato', 1, 0),
(6, 'prof_ingles', '$2y$10$zP2QGvl598XPWZJYpSrLY.Mw2ZwL45ofFsDsBWlcTAsXve5PIZckS', 'PROFESOR', 'David Alejandro', 'Smith Pineda', '1805556667', 'dsmith@ueapd.edu.ec', '0996667778', 'Pinllo, Ambato', 1, 0),
(7, 'prof_edfisica', '$2y$10$zP2QGvl598XPWZJYpSrLY.Mw2ZwL45ofFsDsBWlcTAsXve5PIZckS', 'PROFESOR', 'Fernando Javier', 'Rios Cordero', '1806667778', 'frios@ueapd.edu.ec', '0997778889', 'La Joya, Ambato', 1, 0);

-- Representantes (ID 8 al 17) -> Contraseña: 'rep123'
INSERT INTO `usuarios` (`id`, `username`, `password_hash`, `rol`, `nombres`, `apellidos`, `cedula`, `email`, `telefono`, `direccion`, `activo`, `debe_cambiar_pass`) VALUES
(8, 'rep_mendoza', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Roberto Carlos', 'Mendoza Paredes', '1807778889', 'rep.mendoza@gmail.com', '0981112233', 'Av. Atahualpa 102', 1, 0),
(9, 'rep_perez', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Martha Isabel', 'Perez Guaman', '1808889990', 'rep.perez@gmail.com', '0982223344', 'Calle Guayaquil 405', 1, 0),
(10, 'rep_gomez', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Luis Fernando', 'Gomez Chacon', '1809990001', 'rep.gomez@gmail.com', '0983334455', 'Av. Cevallos 802', 1, 0),
(11, 'rep_torres', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Carmen Rosa', 'Torres Naranjo', '1810001112', 'rep.torres@gmail.com', '0984445566', 'Calle Bolivar 120', 1, 0),
(12, 'rep_sanchez', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Hugo Efrain', 'Sanchez Villacis', '1811112223', 'rep.sanchez@gmail.com', '0985556677', 'Av. Miraflores 301', 1, 0),
(13, 'rep_romero', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Silvia Patricia', 'Romero Acosta', '1812223334', 'rep.romero@gmail.com', '0986667788', 'Calle Sucre 512', 1, 0),
(14, 'rep_morales', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Victor Hugo', 'Morales Espin', '1813334445', 'rep.morales@gmail.com', '0987778899', 'Av. Los Molles 104', 1, 0),
(15, 'rep_castro', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Diana Elizabeth', 'Castro Medina', '1814445556', 'rep.castro@gmail.com', '0988889900', 'Calle Olmedo 702', 1, 0),
(16, 'rep_vargas', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Jaime Ramiro', 'Vargas Carrillo', '1815556667', 'rep.vargas@gmail.com', '0989990011', 'Av. El Rey 220', 1, 0),
(17, 'rep_suarez', '$2y$10$.oeHPfmCqVTiNzJ8ndkuQ.s0CbbTBMdCAinD5hJ3xC/ZHtYpC15qe', 'REPRESENTANTE', 'Veronica Lucía', 'Suarez Barreno', '1816667778', 'rep.suarez@gmail.com', '0980001122', 'Calle Rocafuerte 911', 1, 0);

-- Alumnos (ID 18 al 47) -> Contraseña: 'alumno123'
INSERT INTO `usuarios` (`id`, `username`, `password_hash`, `rol`, `nombres`, `apellidos`, `cedula`, `email`, `telefono`, `direccion`, `activo`, `debe_cambiar_pass`) VALUES
(18, 'alumno1', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Mateo Sebastian', 'Mendoza Perez', '1850000001', 'mateo.mendoza@ueapd.edu.ec', '0970000001', 'Av. Atahualpa 102', 1, 0),
(19, 'alumno2', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Camila Sofia', 'Mendoza Perez', '1850000002', 'camila.mendoza@ueapd.edu.ec', '0970000002', 'Av. Atahualpa 102', 1, 0),
(20, 'alumno3', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Joaquin Nicolas', 'Perez Guaman', '1850000003', 'joaquin.perez@ueapd.edu.ec', '0970000003', 'Calle Guayaquil 405', 1, 0),
(21, 'alumno4', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Valentina Isabel', 'Gomez Chacon', '1850000004', 'valentina.gomez@ueapd.edu.ec', '0970000004', 'Av. Cevallos 802', 1, 0),
(22, 'alumno5', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Gabriel Alexander', 'Torres Naranjo', '1850000005', 'gabriel.torres@ueapd.edu.ec', '0970000005', 'Calle Bolivar 120', 1, 0),
(23, 'alumno6', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Paula Renata', 'Sanchez Villacis', '1850000006', 'paula.sanchez@ueapd.edu.ec', '0970000006', 'Av. Miraflores 301', 1, 0),
(24, 'alumno7', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Daniel Eduardo', 'Romero Acosta', '1850000007', 'daniel.romero@ueapd.edu.ec', '0970000007', 'Calle Sucre 512', 1, 0),
(25, 'alumno8', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Luciana Maria', 'Morales Espin', '1850000008', 'luciana.morales@ueapd.edu.ec', '0970000008', 'Av. Los Molles 104', 1, 0),
(26, 'alumno9', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Ariel David', 'Castro Medina', '1850000009', 'ariel.castro@ueapd.edu.ec', '0970000009', 'Calle Olmedo 702', 1, 0),
(27, 'alumno10', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Doménica Andrea', 'Vargas Carrillo', '1850000010', 'domenica.vargas@ueapd.edu.ec', '0970000010', 'Av. El Rey 220', 1, 0),
(28, 'alumno11', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Emilio Jose', 'Suarez Barreno', '1850000011', 'emilio.suarez@ueapd.edu.ec', '0970000011', 'Calle Rocafuerte 911', 1, 0),
(29, 'alumno12', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Romina Michelle', 'Mendoza Perez', '1850000012', 'romina.mendoza@ueapd.edu.ec', '0970000012', 'Av. Atahualpa 102', 1, 0),
(30, 'alumno13', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Samuel Alejandro', 'Perez Guaman', '1850000013', 'samuel.perez@ueapd.edu.ec', '0970000013', 'Calle Guayaquil 405', 1, 0),
(31, 'alumno14', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Mia Victoria', 'Gomez Chacon', '1850000014', 'mia.gomez@ueapd.edu.ec', '0970000014', 'Av. Cevallos 802', 1, 0),
(32, 'alumno15', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Benjamin Adrian', 'Torres Naranjo', '1850000015', 'benjamin.torres@ueapd.edu.ec', '0970000015', 'Calle Bolivar 120', 1, 0),
(33, 'alumno16', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Martina Daniela', 'Sanchez Villacis', '1850000016', 'martina.sanchez@ueapd.edu.ec', '0970000016', 'Av. Miraflores 301', 1, 0),
(34, 'alumno17', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Lucas Martin', 'Romero Acosta', '1850000017', 'lucas.romero@ueapd.edu.ec', '0970000017', 'Calle Sucre 512', 1, 0),
(35, 'alumno18', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Samantha Nicole', 'Morales Espin', '1850000018', 'samantha.morales@ueapd.edu.ec', '0970000018', 'Av. Los Molles 104', 1, 0),
(36, 'alumno19', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Thiago Javier', 'Castro Medina', '1850000019', 'thiago.castro@ueapd.edu.ec', '0970000019', 'Calle Olmedo 702', 1, 0),
(37, 'alumno20', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Salomé Cristina', 'Vargas Carrillo', '1850000020', 'salome.vargas@ueapd.edu.ec', '0970000020', 'Av. El Rey 220', 1, 0),
(38, 'alumno21', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Ian Andres', 'Suarez Barreno', '1850000021', 'ian.suarez@ueapd.edu.ec', '0970000021', 'Calle Rocafuerte 911', 1, 0),
(39, 'alumno22', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Isabella Patricia', 'Mendoza Perez', '1850000022', 'isabella.mendoza@ueapd.edu.ec', '0970000022', 'Av. Atahualpa 102', 1, 0),
(40, 'alumno23', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Maximiliano Luis', 'Perez Guaman', '1850000023', 'max.perez@ueapd.edu.ec', '0970000023', 'Calle Guayaquil 405', 1, 0),
(41, 'alumno24', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Renata Belen', 'Gomez Chacon', '1850000024', 'renata.gomez@ueapd.edu.ec', '0970000024', 'Av. Cevallos 802', 1, 0),
(42, 'alumno25', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Sebastian Fernando', 'Torres Naranjo', '1850000025', 'seb.torres@ueapd.edu.ec', '0970000025', 'Calle Bolivar 120', 1, 0),
(43, 'alumno26', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Emily Sofia', 'Sanchez Villacis', '1850000026', 'emily.sanchez@ueapd.edu.ec', '0970000026', 'Av. Miraflores 301', 1, 0),
(44, 'alumno27', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Nicolas Esteban', 'Romero Acosta', '1850000027', 'nico.romero@ueapd.edu.ec', '0970000027', 'Calle Sucre 512', 1, 0),
(45, 'alumno28', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Camila Alejandra', 'Morales Espin', '1850000028', 'camila.morales@ueapd.edu.ec', '0970000028', 'Av. Los Molles 104', 1, 0),
(46, 'alumno29', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Javier Ignacio', 'Castro Medina', '1850000029', 'javier.castro@ueapd.edu.ec', '0970000029', 'Calle Olmedo 702', 1, 0),
(47, 'alumno30', '$2y$10$kNqIlFyfBYv8i4DndlueG.y4ZhoZcXvYTBENaD9EYWDgAEqFhjaDS', 'ALUMNO', 'Valeria Monserrath', 'Vargas Carrillo', '1850000030', 'valeria.vargas@ueapd.edu.ec', '0970000030', 'Av. El Rey 220', 1, 0);

-- 3. Período Lectivo y Trimestres
TRUNCATE TABLE `periodos_lectivos`;
INSERT INTO `periodos_lectivos` (`id`, `nombre`, `fecha_inicio`, `fecha_fin`, `activo`, `estado`) VALUES
(1, '2025-2026', '2025-09-01', '2026-06-30', 1, 'ABIERTO');

TRUNCATE TABLE `trimestres`;
INSERT INTO `trimestres` (`id`, `periodo_id`, `numero`, `nombre`, `fecha_inicio`, `fecha_fin`, `estado`) VALUES
(1, 1, 1, 'Primer Trimestre', '2025-09-01', '2025-12-05', 'CERRADO'),
(2, 1, 2, 'Segundo Trimestre', '2025-12-08', '2026-03-13', 'ABIERTO'),
(3, 1, 3, 'Tercer Trimestre', '2026-03-16', '2026-06-30', 'ABIERTO');

-- 4. Cursos, Paralelos y Materias
TRUNCATE TABLE `cursos`;
INSERT INTO `cursos` (`id`, `nombre`, `nivel`) VALUES
(1, '8vo Año EGB', 'EGB Superior'),
(2, '9no Año EGB', 'EGB Superior'),
(3, '10mo Año EGB', 'EGB Superior');

TRUNCATE TABLE `paralelos`;
INSERT INTO `paralelos` (`id`, `curso_id`, `nombre`, `periodo_id`) VALUES
(1, 1, 'A', 1),
(2, 2, 'A', 1),
(3, 3, 'A', 1);

TRUNCATE TABLE `materias`;
INSERT INTO `materias` (`id`, `nombre`, `codigo`, `area`) VALUES
(1, 'Matemáticas', 'MAT-01', 'Exactas'),
(2, 'Lengua y Literatura', 'LEN-01', 'Lenguaje'),
(3, 'Ciencias Naturales', 'CNI-01', 'Naturales'),
(4, 'Estudios Sociales', 'CSO-01', 'Sociales'),
(5, 'Inglés', 'ING-01', 'Idiomas'),
(6, 'Educación Física', 'EDF-01', 'Cultura Física');

-- 5. Perfiles de Profesores, Representantes y Alumnos
TRUNCATE TABLE `profesores`;
INSERT INTO `profesores` (`id`, `usuario_id`, `especialidad`, `titulo`) VALUES
(1, 2, 'Matemáticas y Cálculo', 'Licenciado en Ciencias de la Educación'),
(2, 3, 'Lengua Castellana y Literatura', 'Licenciada en Filología'),
(3, 4, 'Biología y Química', 'Ingeniero Biotecnólogo'),
(4, 5, 'Historia y Ciencias Sociales', 'Licenciada en Ciencias Sociales'),
(5, 6, 'Lingüística Inglesa', 'Magíster en Enseñanza de Inglés'),
(6, 7, 'Deportes y Recreación', 'Licenciado en Cultura Física');

TRUNCATE TABLE `representantes`;
INSERT INTO `representantes` (`id`, `usuario_id`, `parentesco`, `ocupacion`) VALUES
(1, 8, 'Padre', 'Ingeniero Comercial'),
(2, 9, 'Madre', 'Contadora Pública'),
(3, 10, 'Padre', 'Comerciante'),
(4, 11, 'Madre', 'Docente'),
(5, 12, 'Padre', 'Arquitecto'),
(6, 13, 'Madre', 'Enfermera'),
(7, 14, 'Padre', 'Abogado'),
(8, 15, 'Madre', 'Odontóloga'),
(9, 16, 'Padre', 'Empleado Público'),
(10, 17, 'Madre', 'Empresaria');

TRUNCATE TABLE `alumnos`;
INSERT INTO `alumnos` (`id`, `usuario_id`, `codigo_estudiantil`, `fecha_nacimiento`, `genero`) VALUES
(1, 18, 'EST-2025-001', '2012-03-15', 'MASCULINO'),
(2, 19, 'EST-2025-002', '2011-05-20', 'FEMENINO'),
(3, 20, 'EST-2025-003', '2012-01-10', 'MASCULINO'),
(4, 21, 'EST-2025-004', '2012-07-12', 'FEMENINO'),
(5, 22, 'EST-2025-005', '2012-09-08', 'MASCULINO'),
(6, 23, 'EST-2025-006', '2012-11-25', 'FEMENINO'),
(7, 24, 'EST-2025-007', '2012-02-14', 'MASCULINO'),
(8, 25, 'EST-2025-008', '2012-04-18', 'FEMENINO'),
(9, 26, 'EST-2025-009', '2012-06-30', 'MASCULINO'),
(10, 27, 'EST-2025-010', '2012-08-22', 'FEMENINO'),
(11, 28, 'EST-2025-011', '2011-02-11', 'MASCULINO'),
(12, 29, 'EST-2025-012', '2011-04-05', 'FEMENINO'),
(13, 30, 'EST-2025-013', '2011-06-17', 'MASCULINO'),
(14, 31, 'EST-2025-014', '2011-08-29', 'FEMENINO'),
(15, 32, 'EST-2025-015', '2011-10-03', 'MASCULINO'),
(16, 33, 'EST-2025-016', '2011-12-19', 'FEMENINO'),
(17, 34, 'EST-2025-017', '2011-01-23', 'MASCULINO'),
(18, 35, 'EST-2025-018', '2011-03-31', 'FEMENINO'),
(19, 36, 'EST-2025-019', '2011-05-14', 'MASCULINO'),
(20, 37, 'EST-2025-020', '2011-07-28', 'FEMENINO'),
(21, 38, 'EST-2025-021', '2010-01-05', 'MASCULINO'),
(22, 39, 'EST-2025-022', '2010-03-12', 'FEMENINO'),
(23, 40, 'EST-2025-023', '2010-05-24', 'MASCULINO'),
(24, 41, 'EST-2025-024', '2010-07-09', 'FEMENINO'),
(25, 42, 'EST-2025-025', '2010-09-15', 'MASCULINO'),
(26, 43, 'EST-2025-026', '2010-11-21', 'FEMENINO'),
(27, 44, 'EST-2025-027', '2010-02-28', 'MASCULINO'),
(28, 45, 'EST-2025-028', '2010-04-16', 'FEMENINO'),
(29, 46, 'EST-2025-029', '2010-06-08', 'MASCULINO'),
(30, 47, 'EST-2025-030', '2010-08-30', 'FEMENINO');

-- Vinculación Representante -> Alumnos
TRUNCATE TABLE `representante_alumno`;
INSERT INTO `representante_alumno` (`representante_id`, `alumno_id`, `es_principal`) VALUES
(1, 1, 1),
(1, 2, 1),
(1, 12, 1),
(2, 3, 1),
(2, 13, 1),
(3, 4, 1),
(3, 14, 1),
(4, 5, 1),
(4, 15, 1),
(5, 6, 1),
(5, 16, 1),
(6, 7, 1),
(6, 17, 1),
(7, 8, 1),
(7, 18, 1),
(8, 9, 1),
(8, 19, 1),
(9, 10, 1),
(9, 20, 1),
(10, 11, 1),
(10, 21, 1),
(1, 22, 1),
(2, 23, 1),
(3, 24, 1),
(4, 25, 1),
(5, 26, 1),
(6, 27, 1),
(7, 28, 1),
(8, 29, 1),
(9, 30, 1);

-- 6. Matrículas (10 por paralelo: 8vo A, 9no A, 10mo A)
TRUNCATE TABLE `matriculas`;
INSERT INTO `matriculas` (`id`, `alumno_id`, `paralelo_id`, `periodo_id`, `num_matricula`, `fecha_matricula`, `estado`) VALUES
(1, 1, 1, 1, 'MAT-8A-2025-001', '2025-08-15', 'ACTIVA'),
(2, 2, 1, 1, 'MAT-8A-2025-002', '2025-08-15', 'ACTIVA'),
(3, 3, 1, 1, 'MAT-8A-2025-003', '2025-08-15', 'ACTIVA'),
(4, 4, 1, 1, 'MAT-8A-2025-004', '2025-08-15', 'ACTIVA'),
(5, 5, 1, 1, 'MAT-8A-2025-005', '2025-08-15', 'ACTIVA'),
(6, 6, 1, 1, 'MAT-8A-2025-006', '2025-08-15', 'ACTIVA'),
(7, 7, 1, 1, 'MAT-8A-2025-007', '2025-08-15', 'ACTIVA'),
(8, 8, 1, 1, 'MAT-8A-2025-008', '2025-08-15', 'ACTIVA'),
(9, 9, 1, 1, 'MAT-8A-2025-009', '2025-08-15', 'ACTIVA'),
(10, 10, 1, 1, 'MAT-8A-2025-010', '2025-08-15', 'ACTIVA'),

(11, 11, 2, 1, 'MAT-9A-2025-011', '2025-08-16', 'ACTIVA'),
(12, 12, 2, 1, 'MAT-9A-2025-012', '2025-08-16', 'ACTIVA'),
(13, 13, 2, 1, 'MAT-9A-2025-013', '2025-08-16', 'ACTIVA'),
(14, 14, 2, 1, 'MAT-9A-2025-014', '2025-08-16', 'ACTIVA'),
(15, 15, 2, 1, 'MAT-9A-2025-015', '2025-08-16', 'ACTIVA'),
(16, 16, 2, 1, 'MAT-9A-2025-016', '2025-08-16', 'ACTIVA'),
(17, 17, 2, 1, 'MAT-9A-2025-017', '2025-08-16', 'ACTIVA'),
(18, 18, 2, 1, 'MAT-9A-2025-018', '2025-08-16', 'ACTIVA'),
(19, 19, 2, 1, 'MAT-9A-2025-019', '2025-08-16', 'ACTIVA'),
(20, 20, 2, 1, 'MAT-9A-2025-020', '2025-08-16', 'ACTIVA'),

(21, 21, 3, 1, 'MAT-10A-2025-021', '2025-08-17', 'ACTIVA'),
(22, 22, 3, 1, 'MAT-10A-2025-022', '2025-08-17', 'ACTIVA'),
(23, 23, 3, 1, 'MAT-10A-2025-023', '2025-08-17', 'ACTIVA'),
(24, 24, 3, 1, 'MAT-10A-2025-024', '2025-08-17', 'ACTIVA'),
(25, 25, 3, 1, 'MAT-10A-2025-025', '2025-08-17', 'ACTIVA'),
(26, 26, 3, 1, 'MAT-10A-2025-026', '2025-08-17', 'ACTIVA'),
(27, 27, 3, 1, 'MAT-10A-2025-027', '2025-08-17', 'ACTIVA'),
(28, 28, 3, 1, 'MAT-10A-2025-028', '2025-08-17', 'ACTIVA'),
(29, 29, 3, 1, 'MAT-10A-2025-029', '2025-08-17', 'ACTIVA'),
(30, 30, 3, 1, 'MAT-10A-2025-030', '2025-08-17', 'ACTIVA');

-- 7. Asignaciones Docentes (Profesor -> Materia -> Paralelo)
TRUNCATE TABLE `asignaciones_docentes`;
INSERT INTO `asignaciones_docentes` (`id`, `profesor_id`, `materia_id`, `paralelo_id`, `periodo_id`) VALUES
(1, 1, 1, 1, 1),
(2, 2, 2, 1, 1),
(3, 3, 3, 1, 1),
(4, 4, 4, 1, 1),
(5, 5, 5, 1, 1),
(6, 6, 6, 1, 1),

(7, 1, 1, 2, 1),
(8, 2, 2, 2, 1),
(9, 3, 3, 2, 1),
(10, 4, 4, 2, 1),
(11, 5, 5, 2, 1),
(12, 6, 6, 2, 1);

-- 8. Horarios de Clases
TRUNCATE TABLE `horarios`;
INSERT INTO `horarios` (`asignacion_id`, `dia_semana`, `hora_inicio`, `hora_fin`, `aula`) VALUES
(1, 'LUNES', '07:15:00', '08:45:00', 'Aula 8A'),
(1, 'MIERCOLES', '09:15:00', '10:45:00', 'Aula 8A'),
(2, 'MARTES', '07:15:00', '08:45:00', 'Aula 8A'),
(2, 'JUEVES', '09:15:00', '10:45:00', 'Aula 8A'),
(3, 'LUNES', '09:15:00', '10:45:00', 'Laboratorio 1'),
(4, 'MIERCOLES', '07:15:00', '08:45:00', 'Aula 8A'),
(5, 'VIERNES', '07:15:00', '08:45:00', 'Laboratorio de Idiomas'),
(6, 'VIERNES', '09:15:00', '10:45:00', 'Cancha Principal');

-- 9. Componentes de Evaluación
TRUNCATE TABLE `componentes_evaluacion`;
INSERT INTO `componentes_evaluacion` (`id`, `nombre`, `codigo`, `ponderacion_porcentaje`, `descripcion`) VALUES
(1, 'Aportes', 'APORTES', 40.00, 'Tareas, lecciones, trabajos en clase y talleres (40%)'),
(2, 'Proyecto', 'PROYECTO', 30.00, 'Proyecto integrador o interdisciplinar (30%)'),
(3, 'Evaluación', 'EVALUACION', 30.00, 'Evaluación sumativa de trimestre (30%)');

-- 10. Calificaciones de Muestra
TRUNCATE TABLE `calificaciones`;
INSERT INTO `calificaciones` (`alumno_id`, `asignacion_id`, `trimestre_id`, `componente_id`, `nota`, `observacion`, `registrado_por`) VALUES
(1, 1, 1, 1, 9.50, 'Excelente en tareas', 2),
(1, 1, 1, 2, 9.00, 'Proyecto bien sustentado', 2),
(1, 1, 1, 3, 8.50, 'Examen escrito', 2),

(1, 2, 1, 1, 8.80, 'Buen rendimiento', 3),
(1, 2, 1, 2, 9.20, 'Ensayo destacado', 3),
(1, 2, 1, 3, 9.00, 'Prueba de lectura', 3),

(1, 1, 2, 1, 9.00, 'Avances T2', 2),
(1, 1, 2, 2, 8.80, 'Avance proyecto', 2),
(1, 1, 2, 3, NULL, 'Evaluación aún no tomada', 2),

(1, 2, 2, 1, 9.50, 'Sobresaliente en talleres', 3),
(1, 2, 2, 2, NULL, 'Proyecto en desarrollo', 3),
(1, 2, 2, 3, NULL, 'Evaluación pendiente', 3);

-- 11. Tareas y Entregas
TRUNCATE TABLE `tareas`;
INSERT INTO `tareas` (`id`, `asignacion_id`, `trimestre_id`, `titulo`, `descripcion`, `tipo`, `fecha_publicacion`, `fecha_limite`, `archivo_adjunto`) VALUES
(1, 1, 2, 'Ejercicios de Ecuaciones de Primer Grado', 'Resolver los ejercicios de la página 45 a la 48 del texto guía.', 'DEBER', '2026-02-10 08:00:00', '2026-02-17 23:59:00', 'guia_ecuaciones.pdf'),
(2, 2, 2, 'Análisis Literario de Don Quijote', 'Redactar un ensayo de 500 palabras sobre los personajes principales.', 'INVESTIGACION', '2026-02-12 10:00:00', '2026-02-20 23:59:00', 'pauta_ensayo.pdf'),
(3, 3, 2, 'Informe de Laboratorio: Célula Eucariota', 'Presentar gráficos observados en el microscopio con sus partes.', 'TALLER', '2026-02-15 09:00:00', '2026-02-28 23:59:00', NULL);

TRUNCATE TABLE `entregas_tareas`;
INSERT INTO `entregas_tareas` (`tarea_id`, `alumno_id`, `texto_entrega`, `archivo_entrega`, `fecha_entrega`, `estado`, `nota`, `retroalimentacion`, `fecha_calificacion`) VALUES
(1, 1, 'Adjunto resolución de la guía de ejercicios.', 'mateo_mendoza_deber1.pdf', '2026-02-15 16:30:00', 'CALIFICADA', 9.50, 'Muy buen procedimiento y respuestas correctas.', '2026-02-16 10:00:00'),
(2, 1, 'Desarrollo del ensayo sobre el Quijote y Sancho Panza.', 'mateo_mendoza_ensayo.docx', '2026-02-18 20:15:00', 'ENTREGADA', NULL, NULL, NULL);

-- 12. Asistencia Escolar
TRUNCATE TABLE `asistencia`;
INSERT INTO `asistencia` (`id`, `alumno_id`, `paralelo_id`, `trimestre_id`, `fecha`, `estado`, `observacion`, `registrado_por`) VALUES
(1, 1, 1, 2, '2026-02-02', 'PRESENTE', 'Asistencia normal', 2),
(2, 1, 1, 2, '2026-02-03', 'PRESENTE', 'Asistencia normal', 2),
(3, 1, 1, 2, '2026-02-04', 'ATRASO', 'Llegó 15 minutos tarde', 2),
(4, 1, 1, 2, '2026-02-05', 'FALTA_JUSTIFICADA', 'Cita médica justificada por representante', 2),
(5, 1, 1, 2, '2026-02-06', 'PRESENTE', 'Asistencia normal', 2),
(6, 2, 1, 2, '2026-02-02', 'PRESENTE', 'Asistencia normal', 2),
(7, 2, 1, 2, '2026-02-03', 'FALTA_INJUSTIFICADA', 'No asistió a la jornada', 2);

-- 13. Justificativos
TRUNCATE TABLE `justificativos`;
INSERT INTO `justificativos` (`id`, `asistencia_id`, `representante_id`, `motivo`, `archivo_evidencia`, `estado`, `observacion_admin`, `atendido_por`, `fecha_respuesta`) VALUES
(1, 4, 1, 'Cita odontológica programada en el IESS Ambato.', 'certificado_medico_001.pdf', 'APROBADO', 'Certificado validado correctamente.', 1, '2026-02-05 14:00:00'),
(2, 7, 2, 'Calamidad doméstica por fuerte gripal.', 'receta_medica_002.jpg', 'PENDIENTE', NULL, NULL, NULL);

-- 14. Comportamiento y Observaciones
TRUNCATE TABLE `comportamiento`;
INSERT INTO `comportamiento` (`alumno_id`, `trimestre_id`, `calificacion_cualitativa`, `observacion`, `compromiso`, `registrado_por`) VALUES
(1, 1, 'A', 'Estudiante participativo, respetuoso y puntual en todas las actividades.', 'Mantener el liderazgo académico positivo.', 2),
(1, 2, 'A', 'Mantiene un comportamiento excelente en clase.', 'Seguir colaborando en grupos de trabajo.', 2),
(7, 2, 'C', 'Llamados de atención frecuentes por distracciones durante clase.', 'Mejorar concentración y cumplir reglas de convivencia.', 2);

-- 15. Comunicados Informativos
TRUNCATE TABLE `comunicados`;
INSERT INTO `comunicados` (`id`, `titulo`, `contenido`, `remitente_id`, `dirigido_a_rol`, `paralelo_id`) VALUES
(1, 'Convocatoria a Reunión de Padres de Familia', 'Se convoca a la sesión ordinaria de entrega de reportes del Primer Trimestre el día Viernes a las 15:00 en el auditórium del colegio.', 1, 'REPRESENTANTE', NULL),
(2, 'Casa Abierta de Ciencias e Innovación', 'Recordamos a todos los estudiantes de 8vo, 9no y 10mo EGB preparar sus estands para la Casa Abierta institucional.', 1, 'TODOS', NULL);

-- 16. Notificaciones
TRUNCATE TABLE `notificaciones`;
INSERT INTO `notificaciones` (`usuario_id`, `titulo`, `mensaje`, `tipo`, `leido`) VALUES
(18, 'Nueva Tarea Asignada', 'Se ha publicado la tarea: Ejercicios de Ecuaciones de Primer Grado.', 'TAREA', 1),
(8, 'Justificativo Aprobado', 'Su justificativo para Mateo Mendoza del día 2026-02-05 ha sido APROBADO.', 'JUSTIFICATIVO', 0),
(18, 'Tarea Calificada', 'Su tarea de Matemáticas ha sido calificada con 9.50/10.', 'ACADEMICO', 0);

SET FOREIGN_KEY_CHECKS = 1;
