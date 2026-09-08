<?php
/**
 * Test directo del Login Controller y generación de JWT
 */

require_once __DIR__ . '/config/database.php';
require_once __DIR__ . '/src/Security/JwtManager.php';
require_once __DIR__ . '/src/Services/AuditService.php';
require_once __DIR__ . '/src/Repositories/UserRepository.php';

use App\Repositories\UserRepository;
use App\Security\JwtManager;

echo "--- PRUEBA DE LOGIN Y TOKEN JWT ---\n";

$userRepo = new UserRepository();
$user = $userRepo->findByUsername('admin');

if ($user) {
    echo "[OK] Usuario 'admin' encontrado en la tabla 'usuarios'.\n";
    echo "     Nombres: {$user['nombres']} {$user['apellidos']}\n";
    echo "     Rol: {$user['rol']}\n";
    echo "     Cédula: {$user['cedula']}\n";

    $config = require __DIR__ . '/config/config.php';
    $payload = [
        'sub' => $user['id'],
        'username' => $user['username'],
        'rol' => $user['rol'],
        'nombres' => $user['nombres'],
        'apellidos' => $user['apellidos']
    ];

    $token = JwtManager::encode($payload, $config['jwt']['secret'], $config['jwt']['access_ttl']);
    echo "\n[OK] Generación de Token JWT exitosa:\n";
    echo "     Token: " . substr($token, 0, 45) . "...\n";

    $decoded = JwtManager::decode($token, $config['jwt']['secret']);
    if ($decoded && $decoded['sub'] == $user['id']) {
        echo "[OK] Decodificación y validación de firma JWT correcta.\n";
    } else {
        echo "[ERROR] Falló la decodificación de JWT.\n";
    }
} else {
    echo "[ERROR] Usuario 'admin' no existe en la BD.\n";
}

echo "\n--- PRUEBA COMPLETADA ---\n";
