<?php

namespace App\Repositories;

use App\Config\Database;
use PDO;
use Exception;

class JustificationRepository {
    private PDO $db;

    public function __construct() {
        $this->db = Database::getConnection();
    }

    public function createJustification(int $asistenciaId, int $representanteId, string $motivo, ?string $archivo): int {
        $sql = "INSERT INTO justificativos (asistencia_id, representante_id, motivo, archivo_evidencia, estado, fecha_envio)
                VALUES (:asistencia_id, :representante_id, :motivo, :archivo, 'PENDIENTE', NOW())";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            'asistencia_id' => $asistenciaId,
            'representante_id' => $representanteId,
            'motivo' => $motivo,
            'archivo' => $archivo
        ]);
        return (int)$this->db->lastInsertId();
    }

    public function getJustificationsForParent(int $representanteUserId): array {
        $sql = "
            SELECT j.id as justificativo_id, j.motivo, j.archivo_evidencia, j.estado, j.observacion_admin, j.fecha_envio, j.fecha_respuesta,
                   a.fecha as fecha_asistencia, a.estado as estado_asistencia_original,
                   u_est.nombres as alumno_nombres, u_est.apellidos as alumno_apellidos
            FROM justificativos j
            JOIN asistencia a ON j.asistencia_id = a.id
            JOIN alumnos est ON a.alumno_id = est.id
            JOIN usuarios u_est ON est.usuario_id = u_est.id
            JOIN representantes r ON j.representante_id = r.id
            WHERE r.usuario_id = :repUserId
            ORDER BY j.fecha_envio DESC
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute(['repUserId' => $representanteUserId]);
        return $stmt->fetchAll();
    }

    public function getPendingJustificationsForAdmin(): array {
        $sql = "
            SELECT j.id as justificativo_id, j.motivo, j.archivo_evidencia, j.estado, j.fecha_envio,
                   a.id as asistencia_id, a.fecha as fecha_asistencia, a.estado as estado_asistencia,
                   u_est.nombres as alumno_nombres, u_est.apellidos as alumno_apellidos,
                   u_rep.nombres as rep_nombres, u_rep.apellidos as rep_apellidos
            FROM justificativos j
            JOIN asistencia a ON j.asistencia_id = a.id
            JOIN alumnos est ON a.alumno_id = est.id
            JOIN usuarios u_est ON est.usuario_id = u_est.id
            JOIN representantes r ON j.representante_id = r.id
            JOIN usuarios u_rep ON r.usuario_id = u_rep.id
            ORDER BY j.fecha_envio DESC
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public function processJustification(int $justificativoId, string $nuevoEstado, ?string $observacionAdmin, int $atendidoPor): bool {
        $this->db->beginTransaction();
        try {
            // 1. Obtener justificativo y registro de asistencia
            $stmtJust = $this->db->prepare("SELECT asistencia_id, estado FROM justificativos WHERE id = :id FOR UPDATE");
            $stmtJust->execute(['id' => $justificativoId]);
            $just = $stmtJust->fetch();

            if (!$just) {
                $this->db->rollBack();
                return false;
            }

            // 2. Actualizar estado del justificativo
            $stmtUpJust = $this->db->prepare("
                UPDATE justificativos
                SET estado = :estado, observacion_admin = :obs, atendido_por = :admin, fecha_respuesta = NOW()
                WHERE id = :id
            ");
            $stmtUpJust->execute([
                'id' => $justificativoId,
                'estado' => $nuevoEstado,
                'obs' => $observacionAdmin,
                'admin' => $atendidoPor
            ]);

            // 3. Si es APROBADO, actualizar la asistencia de FALTA_INJUSTIFICADA o ATRASO a FALTA_JUSTIFICADA
            if ($nuevoEstado === 'APROBADO') {
                $stmtUpAsis = $this->db->prepare("
                    UPDATE asistencia
                    SET estado = 'FALTA_JUSTIFICADA', observacion = CONCAT(COALESCE(observacion, ''), ' [Justificado por Admin]')
                    WHERE id = :asistencia_id
                ");
                $stmtUpAsis->execute(['asistencia_id' => $just['asistencia_id']]);
            }

            $this->db->commit();
            return true;
        } catch (Exception $e) {
            $this->db->rollBack();
            error_log("Error procesando justificativo: " . $e->getMessage());
            return false;
        }
    }
}
