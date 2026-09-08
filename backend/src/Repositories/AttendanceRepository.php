<?php

namespace App\Repositories;

use App\Config\Database;
use PDO;

class AttendanceRepository {
    private PDO $db;

    public function __construct() {
        $this->db = Database::getConnection();
    }

    public function getAttendanceSummaryForStudent(int $alumnoId, ?int $trimestreId = null): array {
        $sql = "
            SELECT
                COUNT(*) as total_dias,
                SUM(IF(estado = 'PRESENTE', 1, 0)) as presentes,
                SUM(IF(estado = 'ATRASO', 1, 0)) as atrasos,
                SUM(IF(estado = 'FALTA_INJUSTIFICADA', 1, 0)) as faltas_injustificadas,
                SUM(IF(estado = 'FALTA_JUSTIFICADA', 1, 0)) as faltas_justificadas
            FROM asistencia
            WHERE alumno_id = :alumnoId
        ";
        $params = ['alumnoId' => $alumnoId];
        if ($trimestreId !== null) {
            $sql .= " AND trimestre_id = :trimestreId";
            $params['trimestreId'] = $trimestreId;
        }

        $stmt = $this->db->prepare($sql);
        $stmt->execute($params);
        return $stmt->fetch() ?: [
            'total_dias' => 0, 'presentes' => 0, 'atrasos' => 0, 'faltas_injustificadas' => 0, 'faltas_justificadas' => 0
        ];
    }

    public function getAttendanceRecordsForStudent(int $alumnoId): array {
        $sql = "
            SELECT a.id, a.fecha, a.estado, a.observacion, t.nombre as trimestre_nombre,
                   j.id as justificativo_id, j.estado as estado_justificativo, j.motivo as motivo_justificativo
            FROM asistencia a
            JOIN trimestres t ON a.trimestre_id = t.id
            LEFT JOIN justificativos j ON j.asistencia_id = a.id
            WHERE a.alumno_id = :alumnoId
            ORDER BY a.fecha DESC
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute(['alumnoId' => $alumnoId]);
        return $stmt->fetchAll();
    }

    public function upsertAttendanceRecord(int $alumnoId, int $paraleloId, int $trimestreId, string $fecha, string $estado, ?string $observacion, int $registradoPor): bool {
        $sql = "
            INSERT INTO asistencia (alumno_id, paralelo_id, trimestre_id, fecha, estado, observacion, registrado_por, version)
            VALUES (:alumno_id, :paralelo_id, :trimestre_id, :fecha, :estado, :observacion, :registrado_por, 1)
            ON DUPLICATE KEY UPDATE
                estado = VALUES(estado),
                observacion = VALUES(observacion),
                registrado_por = VALUES(registrado_por),
                version = version + 1
        ";
        $stmt = $this->db->prepare($sql);
        return $stmt->execute([
            'alumno_id' => $alumnoId,
            'paralelo_id' => $paraleloId,
            'trimestre_id' => $trimestreId,
            'fecha' => $fecha,
            'estado' => $estado,
            'observacion' => $observacion,
            'registrado_por' => $registradoPor
        ]);
    }
}
