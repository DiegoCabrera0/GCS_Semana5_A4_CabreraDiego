<?php

namespace App\Repositories;

use App\Config\Database;
use PDO;

class UserRepository {
    private PDO $db;

    public function __construct() {
        $this->db = Database::getConnection();
    }

    public function findByUsername(string $username): ?array {
        $stmt = $this->db->prepare("SELECT * FROM usuarios WHERE username = :username LIMIT 1");
        $stmt->execute(['username' => $username]);
        $user = $stmt->fetch();
        return $user ?: null;
    }

    public function findById(int $id): ?array {
        $stmt = $this->db->prepare("SELECT id, username, rol, nombres, apellidos, cedula, email, telefono, direccion, activo, debe_cambiar_pass FROM usuarios WHERE id = :id LIMIT 1");
        $stmt->execute(['id' => $id]);
        $user = $stmt->fetch();
        return $user ?: null;
    }

    public function getAll(array $filters = []): array {
        $sql = "SELECT id, username, rol, nombres, apellidos, cedula, email, telefono, direccion, activo, fecha_creacion FROM usuarios WHERE 1=1";
        $params = [];

        if (!empty($filters['rol'])) {
            $sql .= " AND rol = :rol";
            $params['rol'] = $filters['rol'];
        }

        if (isset($filters['activo'])) {
            $sql .= " AND activo = :activo";
            $params['activo'] = $filters['activo'];
        }

        $sql .= " ORDER BY apellidos, nombres";

        $stmt = $this->db->prepare($sql);
        $stmt->execute($params);
        return $stmt->fetchAll();
    }

    public function create(array $data): int {
        $sql = "INSERT INTO usuarios (username, password_hash, rol, nombres, apellidos, cedula, email, telefono, direccion, activo, debe_cambiar_pass)
                VALUES (:username, :password_hash, :rol, :nombres, :apellidos, :cedula, :email, :telefono, :direccion, :activo, :debe_cambiar_pass)";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            'username' => $data['username'],
            'password_hash' => $data['password_hash'],
            'rol' => $data['rol'],
            'nombres' => $data['nombres'],
            'apellidos' => $data['apellidos'],
            'cedula' => $data['cedula'],
            'email' => $data['email'],
            'telefono' => $data['telefono'] ?? null,
            'direccion' => $data['direccion'] ?? null,
            'activo' => $data['activo'] ?? 1,
            'debe_cambiar_pass' => $data['debe_cambiar_pass'] ?? 0
        ]);
        return (int)$this->db->lastInsertId();
    }

    public function update(int $id, array $data): bool {
        $sql = "UPDATE usuarios SET nombres = :nombres, apellidos = :apellidos, email = :email, telefono = :telefono, direccion = :direccion, activo = :activo WHERE id = :id";
        $stmt = $this->db->prepare($sql);
        return $stmt->execute([
            'id' => $id,
            'nombres' => $data['nombres'],
            'apellidos' => $data['apellidos'],
            'email' => $data['email'],
            'telefono' => $data['telefono'] ?? null,
            'direccion' => $data['direccion'] ?? null,
            'activo' => $data['activo'] ?? 1
        ]);
    }

    public function updatePassword(int $id, string $newPasswordHash, bool $forceChange = false): bool {
        $stmt = $this->db->prepare("UPDATE usuarios SET password_hash = :hash, debe_cambiar_pass = :force WHERE id = :id");
        return $stmt->execute([
            'id' => $id,
            'hash' => $newPasswordHash,
            'force' => $forceChange ? 1 : 0
        ]);
    }

    public function delete(int $id): bool {
        // Soft delete para mantener historial académico
        $stmt = $this->db->prepare("UPDATE usuarios SET activo = 0 WHERE id = :id");
        return $stmt->execute(['id' => $id]);
    }
}
