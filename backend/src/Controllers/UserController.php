<?php

namespace App\Controllers;

use App\Repositories\UserRepository;
use App\Services\AuditService;

class UserController {
    public function index(): void {
        $userRepo = new UserRepository();
        $rolFilter = $_GET['rol'] ?? null;
        $users = $userRepo->getAll(['rol' => $rolFilter]);

        http_response_code(200);
        echo json_encode(['success' => true, 'data' => $users, 'message' => 'Usuarios obtenidos']);
    }

    public function store(array $currentUser): void {
        $input = json_decode(file_get_contents('php://input'), true);

        if (empty($input['username']) || empty($input['cedula']) || empty($input['nombres']) || empty($input['apellidos']) || empty($input['rol'])) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'Campos obligatorios incompletos', 'data' => null]);
            return;
        }

        $userRepo = new UserRepository();
        $tempPassword = $input['password'] ?? 'Temporal123!';
        $data = [
            'username' => trim($input['username']),
            'password_hash' => password_hash($tempPassword, PASSWORD_BCRYPT),
            'rol' => $input['rol'],
            'nombres' => trim($input['nombres']),
            'apellidos' => trim($input['apellidos']),
            'cedula' => trim($input['cedula']),
            'email' => trim($input['email'] ?? strtolower($input['username']) . '@ueapd.edu.ec'),
            'telefono' => $input['telefono'] ?? null,
            'direccion' => $input['direccion'] ?? null,
            'activo' => 1,
            'debe_cambiar_pass' => 1
        ];

        try {
            $newId = $userRepo->create($data);
            AuditService::log($currentUser['id'], 'CREATE_USER', 'usuarios', $newId, ['username' => $data['username']]);

            http_response_code(201);
            echo json_encode(['success' => true, 'message' => 'Usuario creado exitosamente', 'data' => ['id' => $newId]]);
        } catch (\Throwable $e) {
            http_response_code(409);
            echo json_encode(['success' => false, 'message' => 'Error: Cédula, usuario o email ya registrado.', 'data' => null]);
        }
    }

    public function resetPassword(int $userId, array $currentUser): void {
        $tempPassword = 'Temp' . rand(1000, 9999) . '!';
        $userRepo = new UserRepository();
        $userRepo->updatePassword($userId, password_hash($tempPassword, PASSWORD_BCRYPT), true);

        AuditService::log($currentUser['id'], 'RESET_PASSWORD', 'usuarios', $userId);

        http_response_code(200);
        echo json_encode(['success' => true, 'message' => 'Contraseña reestablecida', 'data' => ['temp_password' => $tempPassword]]);
    }

    public function delete(int $userId, array $currentUser): void {
        $userRepo = new UserRepository();
        $userRepo->delete($userId);

        AuditService::log($currentUser['id'], 'SOFT_DELETE_USER', 'usuarios', $userId);

        http_response_code(200);
        echo json_encode(['success' => true, 'message' => 'Usuario desactivado correctamente', 'data' => null]);
    }
}
