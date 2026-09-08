<?php

namespace App\Repositories;

use App\Config\Database;
use PDO;

class StudentRepository {
    private PDO $db;

    public function __construct() {
        $this->db = Database::getConnection();
    }

    public function findByUserId(int $userId): ?array {
        $sql = "
            SELECT a.id as alumno_id, u.id as usuario_id, u.nombres, u.apellidos, u.cedula, u.email,
                   a.codigo_estudiantil, m.id as matricula_id, p.id as paralelo_id, p.nombre as paralelo,
                   c.id as curso_id, c.nombre as curso, pl.id as periodo_id, pl.nombre as periodo
            FROM alumnos a
            JOIN usuarios u ON a.usuario_id = u.id
            LEFT JOIN matriculas m ON a.id = m.alumno_id AND m.estado = 'ACTIVA'
            LEFT JOIN paralelos p ON m.paralelo_id = p.id
            LEFT JOIN cursos c ON p.curso_id = c.id
            LEFT JOIN periodos_lectivos pl ON m.periodo_id = pl.id
            WHERE u.id = :userId AND u.activo = 1
            LIMIT 1
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute(['userId' => $userId]);
        $res = $stmt->fetch();
        return $res ?: null;
    }

    public function getStudentsByParentUserId(int $parentUserId): array {
        $sql = "
            SELECT a.id as alumno_id, u.id as usuario_id, u.nombres, u.apellidos, u.cedula,
                   a.codigo_estudiantil, p.nombre as paralelo, c.nombre as curso, pl.nombre as periodo
            FROM representantes r
            JOIN representante_alumno ra ON r.id = ra.representante_id
            JOIN alumnos a ON ra.alumno_id = a.id
            JOIN usuarios u ON a.usuario_id = u.id
            LEFT JOIN matriculas m ON a.id = m.alumno_id AND m.estado = 'ACTIVA'
            LEFT JOIN paralelos p ON m.paralelo_id = p.id
            LEFT JOIN cursos c ON p.curso_id = c.id
            LEFT JOIN periodos_lectivos pl ON m.periodo_id = pl.id
            WHERE r.usuario_id = :parentUserId AND u.activo = 1
            ORDER BY u.apellidos, u.nombres
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute(['parentUserId' => $parentUserId]);
        return $stmt->fetchAll();
    }

    public function getStudentsByParalelo(int $paraleloId): array {
        $sql = "
            SELECT a.id as alumno_id, u.id as usuario_id, u.nombres, u.apellidos, u.cedula, a.codigo_estudiantil
            FROM alumnos a
            JOIN usuarios u ON a.usuario_id = u.id
            JOIN matriculas m ON a.id = m.alumno_id
            WHERE m.paralelo_id = :paraleloId AND m.estado = 'ACTIVA' AND u.activo = 1
            ORDER BY u.apellidos, u.nombres
        ";
        $stmt = $this->db->prepare($sql);
        $stmt->execute(['paraleloId' => $paraleloId]);
        return $stmt->fetchAll();
    }
}
