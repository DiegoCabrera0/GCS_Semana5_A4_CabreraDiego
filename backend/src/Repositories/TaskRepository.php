<?php

namespace App\Repositories;

use App\Config\Database;
use PDO;

class TaskRepository {
    private PDO $db;

    public function __construct() {
        $this->db = Database::getConnection();
    }

    public function getTasksForStudent(int $alumnoId): array {
        $sql = "
            SELECT t.id as tarea_id, t.titulo, t.descripcion, t.tipo, t.fecha_publicacion, t.fecha_limite, t.archivo_adjunto,
                   m.nombre as materia, m.id as materia_id, u_prof.nombres as profesor_nombres, u_prof.apellidos as profesor_apellidos,
                   et.id as entrega_id, et.texto_entrega, et.archivo_entrega, et.fecha_entrega, et.nota, et.retroalimentacion,
                   COALESCE(et.estado, IF(t.fecha_limite < NOW(), 'VENCIDA', 'PENDIENTE')) as estado
            FROM tareas t
            JOIN asignaciones_docentes ad ON t.asignacion_id = ad.id
            JOIN materias m ON ad.materia_id = m.id
            JOIN profesores prof ON ad.profesor_id = prof.id
            JOIN usuarios u_prof ON prof.usuario_id = u_prof.id
            JOIN matriculas mat ON ad.paralelo_id = mat.paralelo_id AND ad.periodo_id = mat.periodo_id
            LEFT JOIN entregas_tareas et ON et.tarea_id = t.id AND et.alumno_id = mat.alumno_id
            WHERE mat.alumno_id = :alumnoId AND mat.estado = 'ACTIVA'
            ORDER BY m.nombre, t.fecha_limite DESC
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute(['alumnoId' => $alumnoId]);
        return $stmt->fetchAll();
    }

    public function getTasksForTeacher(int $profesorUserId): array {
        $sql = "
            SELECT t.id as tarea_id, t.titulo, t.descripcion, t.tipo, t.fecha_publicacion, t.fecha_limite,
                   m.nombre as materia, c.nombre as curso, p.nombre as paralelo,
                   (SELECT COUNT(*) FROM entregas_tareas WHERE tarea_id = t.id) as total_entregadas
            FROM tareas t
            JOIN asignaciones_docentes ad ON t.asignacion_id = ad.id
            JOIN profesores prof ON ad.profesor_id = prof.id
            JOIN materias m ON ad.materia_id = m.id
            JOIN paralelos p ON ad.paralelo_id = p.id
            JOIN cursos c ON p.curso_id = c.id
            WHERE prof.usuario_id = :profesorUserId
            ORDER BY t.fecha_limite DESC
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute(['profesorUserId' => $profesorUserId]);
        return $stmt->fetchAll();
    }

    public function createTask(array $data): int {
        $sql = "INSERT INTO tareas (asignacion_id, trimestre_id, titulo, descripcion, tipo, fecha_limite, archivo_adjunto)
                VALUES (:asignacion_id, :trimestre_id, :titulo, :descripcion, :tipo, :fecha_limite, :archivo_adjunto)";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            'asignacion_id' => $data['asignacion_id'],
            'trimestre_id' => $data['trimestre_id'],
            'titulo' => $data['titulo'],
            'descripcion' => $data['descripcion'],
            'tipo' => $data['tipo'] ?? 'DEBER',
            'fecha_limite' => $data['fecha_limite'],
            'archivo_adjunto' => $data['archivo_adjunto'] ?? null
        ]);
        return (int)$this->db->lastInsertId();
    }

    public function submitTask(int $tareaId, int $alumnoId, ?string $texto, ?string $archivo): bool {
        $sql = "
            INSERT INTO entregas_tareas (tarea_id, alumno_id, texto_entrega, archivo_entrega, fecha_entrega, estado)
            VALUES (:tarea_id, :alumno_id, :texto, :archivo, NOW(), 'ENTREGADA')
            ON DUPLICATE KEY UPDATE
                texto_entrega = VALUES(texto_entrega),
                archivo_entrega = VALUES(archivo_entrega),
                fecha_entrega = NOW(),
                estado = 'ENTREGADA'
        ";
        $stmt = $this->db->prepare($sql);
        return $stmt->execute([
            'tarea_id' => $tareaId,
            'alumno_id' => $alumnoId,
            'texto' => $texto,
            'archivo' => $archivo
        ]);
    }

    public function gradeSubmission(int $entregaId, float $nota, ?string $retroalimentacion): bool {
        $sql = "UPDATE entregas_tareas SET nota = :nota, retroalimentacion = :retro, estado = 'CALIFICADA', fecha_calificacion = NOW() WHERE id = :id";
        $stmt = $this->db->prepare($sql);
        return $stmt->execute([
            'id' => $entregaId,
            'nota' => $nota,
            'retro' => $retroalimentacion
        ]);
    }
}
