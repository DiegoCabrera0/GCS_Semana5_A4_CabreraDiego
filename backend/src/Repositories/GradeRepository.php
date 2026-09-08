<?php

namespace App\Repositories;

use App\Config\Database;
use PDO;

class GradeRepository {
    private PDO $db;

    public function __construct() {
        $this->db = Database::getConnection();
    }

    public function getGradesForStudent(int $alumnoId, ?int $trimestreId = null): array {
        $sql = "
            SELECT m.id as materia_id, m.nombre as materia, m.codigo as materia_codigo,
                   u_prof.nombres as profesor_nombres, u_prof.apellidos as profesor_apellidos,
                   t.id as trimestre_id, t.numero as trimestre_num, t.nombre as trimestre_nombre,
                   ce.codigo as componente_codigo, ce.nombre as componente_nombre,
                   c.nota, c.observacion
            FROM asignaciones_docentes ad
            JOIN materias m ON ad.materia_id = m.id
            JOIN profesores prof ON ad.profesor_id = prof.id
            JOIN usuarios u_prof ON prof.usuario_id = u_prof.id
            JOIN matriculas mat ON ad.paralelo_id = mat.paralelo_id AND ad.periodo_id = mat.periodo_id
            JOIN trimestres t ON t.periodo_id = ad.periodo_id
            CROSS JOIN componentes_evaluacion ce
            LEFT JOIN calificaciones c ON c.alumno_id = mat.alumno_id
                                      AND c.asignacion_id = ad.id
                                      AND c.trimestre_id = t.id
                                      AND c.componente_id = ce.id
            WHERE mat.alumno_id = :alumnoId
        ";

        $params = ['alumnoId' => $alumnoId];
        if ($trimestreId !== null) {
            $sql .= " AND t.id = :trimestreId";
            $params['trimestreId'] = $trimestreId;
        }

        $sql .= " ORDER BY m.nombre, t.numero, ce.id";

        $stmt = $this->db->prepare($sql);
        $stmt->execute($params);
        return $stmt->fetchAll();
    }

    public function getGradesMatrixForTeacher(int $asignacionId, int $trimestreId): array {
        $sql = "
            SELECT a.id as alumno_id, u.nombres, u.apellidos, a.codigo_estudiantil,
                   c_ap.nota as nota_aportes, c_pr.nota as nota_proyecto, c_ev.nota as nota_evaluacion
            FROM matriculas m
            JOIN alumnos a ON m.alumno_id = a.id
            JOIN usuarios u ON a.usuario_id = u.id
            JOIN asignaciones_docentes ad ON ad.paralelo_id = m.paralelo_id AND ad.periodo_id = m.periodo_id
            LEFT JOIN calificaciones c_ap ON c_ap.alumno_id = a.id AND c_ap.asignacion_id = ad.id AND c_ap.trimestre_id = :trimestreId1 AND c_ap.componente_id = 1
            LEFT JOIN calificaciones c_pr ON c_pr.alumno_id = a.id AND c_pr.asignacion_id = ad.id AND c_pr.trimestre_id = :trimestreId2 AND c_pr.componente_id = 2
            LEFT JOIN calificaciones c_ev ON c_ev.alumno_id = a.id AND c_ev.asignacion_id = ad.id AND c_ev.trimestre_id = :trimestreId3 AND c_ev.componente_id = 3
            WHERE ad.id = :asignacionId AND m.estado = 'ACTIVA' AND u.activo = 1
            ORDER BY u.apellidos, u.nombres
        ";

        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            'asignacionId' => $asignacionId,
            'trimestreId1' => $trimestreId,
            'trimestreId2' => $trimestreId,
            'trimestreId3' => $trimestreId
        ]);
        return $stmt->fetchAll();
    }

    public function upsertGrade(int $alumnoId, int $asignacionId, int $trimestreId, int $componenteId, ?float $nota, ?string $observacion, int $registradoPor): bool {
        $sql = "
            INSERT INTO calificaciones (alumno_id, asignacion_id, trimestre_id, componente_id, nota, observacion, registrado_por)
            VALUES (:alumno_id, :asignacion_id, :trimestre_id, :componente_id, :nota, :observacion, :registrado_por)
            ON DUPLICATE KEY UPDATE
                nota = VALUES(nota),
                observacion = VALUES(observacion),
                registrado_por = VALUES(registrado_por),
                fecha_registro = CURRENT_TIMESTAMP
        ";
        $stmt = $this->db->prepare($sql);
        return $stmt->execute([
            'alumno_id' => $alumnoId,
            'asignacion_id' => $asignacionId,
            'trimestre_id' => $trimestreId,
            'componente_id' => $componenteId,
            'nota' => $nota,
            'observacion' => $observacion,
            'registrado_por' => $registradoPor
        ]);
    }
}
