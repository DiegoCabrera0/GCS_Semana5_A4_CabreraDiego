<?php

namespace App\Repositories;

use App\Config\Database;
use PDO;

class AnnouncementRepository {
    private PDO $db;

    public function __construct() {
        $this->db = Database::getConnection();
    }

    public function getAnnouncementsForUser(string $userRole, ?int $paraleloId = null): array {
        $sql = "
            SELECT c.id, c.titulo, c.contenido, c.dirigido_a_rol, c.fecha_publicacion,
                   u.nombres as remitente_nombres, u.apellidos as remitente_apellidos, u.rol as remitente_rol
            FROM comunicados c
            JOIN usuarios u ON c.remitente_id = u.id
            WHERE (c.dirigido_a_rol = 'TODOS' OR c.dirigido_a_rol = :userRole)
              AND (c.paralelo_id IS NULL OR c.paralelo_id = :paraleloId)
            ORDER BY c.fecha_publicacion DESC
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            'userRole' => $userRole,
            'paraleloId' => $paraleloId
        ]);
        return $stmt->fetchAll();
    }

    public function createAnnouncement(array $data): int {
        $sql = "INSERT INTO comunicados (titulo, contenido, remitente_id, dirigido_a_rol, paralelo_id, fecha_publicacion)
                VALUES (:titulo, :contenido, :remitente_id, :dirigido_a_rol, :paralelo_id, NOW())";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            'titulo' => $data['titulo'],
            'contenido' => $data['contenido'],
            'remitente_id' => $data['remitente_id'],
            'dirigido_a_rol' => $data['dirigido_a_rol'] ?? 'TODOS',
            'paralelo_id' => $data['paralelo_id'] ?? null
        ]);
        return (int)$this->db->lastInsertId();
    }
}
