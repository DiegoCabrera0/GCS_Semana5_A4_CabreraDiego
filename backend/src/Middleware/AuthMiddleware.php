<?php

namespace App\Middleware;

use App\Security\JwtManager;
use App\Repositories\UserRepository;

class AuthMiddleware {
    public static function authenticate(): ?array {
        $headers = getallheaders();
        $authHeader = $headers['Authorization'] ?? $headers['authorization'] ?? $_SERVER['HTTP_AUTHORIZATION'] ?? null;

        if (!$authHeader || !preg_match('/Bearer\s+(.*)$/i', $authHeader, $matches)) {
            return null;
        }

        $token = $matches[1];
        $config = require __DIR__ . '/../../config/config.php';
        $decoded = JwtManager::decode($token, $config['jwt']['secret']);

        if (!$decoded || !isset($decoded['sub'])) {
            return null;
        }

        // Verificar en DB que el usuario exista y siga activo
        $userRepo = new UserRepository();
        $user = $userRepo->findById((int)$decoded['sub']);

        if (!$user || $user['activo'] != 1) {
            return null;
        }

        return $user;
    }
}
