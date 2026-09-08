<?php
/**
 * Configuración Principal del Backend PHP REST API
 * Unidad Educativa Dr. Alfredo Pareja Diezcanseco
 */

// Desactivar despliegue de errores HTML en producción para no romper JSON
ini_set('display_errors', '0');
error_reporting(E_ALL);

return [
    'app' => [
        'name' => 'Sistema de Gestión Académica - UEAPD',
        'env' => 'development',
        'base_url' => '/gestion_academica/api/v1',
        'timezone' => 'America/Guayaquil',
        'storage_path' => __DIR__ . '/../storage',
        'logs_path' => __DIR__ . '/../storage/logs/app.log',
    ],
    'database' => [
        'host' => '127.0.0.1',
        'port' => 3306,
        'dbname' => 'gestion_academica_ueapd',
        'username' => 'root', // O usuario técnico limitado configurado
        'password' => '',
        'charset' => 'utf8mb4'
    ],
    'jwt' => [
        'secret' => 'UEAPD_SECRET_KEY_AMBATO_2026_GRUPO9_VERY_SECURE_KEY',
        'algo' => 'HS256',
        'access_ttl' => 3600 * 8, // 8 horas
        'refresh_ttl' => 86400 * 30, // 30 días
        'issuer' => 'ueapd_api_v1'
    ],
    'upload' => [
        'max_file_size' => 10 * 1024 * 1024, // 10 MB
        'allowed_extensions' => ['pdf', 'doc', 'docx', 'jpg', 'jpeg', 'png'],
        'private_dir' => __DIR__ . '/../storage/private'
    ]
];
