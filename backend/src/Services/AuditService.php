<?php

namespace App\Services;

use App\Config\Database;
use PDO;

class AuditService {
    public static function log(?int $usuarioId, string $accion, string $tabla, ?int $registroId = null, ?array $detalles = null): void {
        try {
            $db = Database::getConnection();
            $ip = $_SERVER['REMOTE_ADDR'] ?? '127.0.0.1';
            $stmt = $db->prepare("
                INSERT INTO auditoria (usuario_id, accion, tabla_afectada, registro_id, detalles, ip)
                VALUES (:usuario_id, :accion, :tabla, :registro_id, :detalles, :ip)
            ");
            $stmt->execute([
                'usuario_id' => $usuarioId,
                'accion' => $accion,
                'tabla' => $tabla,
                'registro_id' => $registroId,
                'detalles' => $detalles ? json_encode($detalles) : null,
                'ip' => $ip
            ]);
        } catch (\Throwable $e) {
            error_log("Error guardando auditoría: " . $e->getMessage());
        }
    }
}
