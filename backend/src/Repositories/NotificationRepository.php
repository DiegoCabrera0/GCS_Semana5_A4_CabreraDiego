<?php

namespace App\Repositories;

use App\Config\Database;
use PDO;

class NotificationRepository {
    private PDO $db;

    public function __construct() {
        $this->db = Database::getConnection();
    }

    public function getNotificationsForUser(int $userId): array {
        $sql = "SELECT id, titulo, mensaje, tipo, leido, fecha_creacion FROM notificaciones WHERE usuario_id = :userId ORDER BY fecha_creacion DESC";
        $stmt = $this->db->prepare($sql);
        $stmt->execute(['userId' => $userId]);
        return $stmt->fetchAll();
    }

    public function markAsRead(int $notificationId, int $userId): bool {
        $sql = "UPDATE notificaciones SET leido = 1 WHERE id = :id AND usuario_id = :userId";
        $stmt = $this->db->prepare($sql);
        return $stmt->execute(['id' => $notificationId, 'userId' => $userId]);
    }

    public function createNotification(int $userId, string $titulo, string $mensaje, string $tipo = 'INFO'): int {
        $sql = "INSERT INTO notificaciones (usuario_id, titulo, mensaje, tipo, leido, fecha_creacion) VALUES (:usuario_id, :titulo, :mensaje, :tipo, 0, NOW())";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            'usuario_id' => $userId,
            'titulo' => $titulo,
            'mensaje' => $mensaje,
            'tipo' => $tipo
        ]);
        return (int)$this->db->lastInsertId();
    }
}
