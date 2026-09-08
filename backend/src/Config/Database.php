<?php

namespace App\Config;

use PDO;
use PDOException;

class Database {
    private static ?PDO $instance = null;

    public static function getConnection(): PDO {
        if (self::$instance === null) {
            // Localizar archivo config.php de forma robusta
            $configPath = __DIR__ . '/../../config/config.php';
            if (!file_exists($configPath)) {
                $configPath = __DIR__ . '/../config/config.php';
            }
            if (!file_exists($configPath)) {
                $configPath = __DIR__ . '/../../config.php';
            }

            $config = file_exists($configPath) ? require $configPath : [];
            $db = $config['database'] ?? [
                'host' => '127.0.0.1',
                'port' => 3306,
                'dbname' => 'gestion_academica_ueapd',
                'username' => 'root',
                'password' => '',
                'charset' => 'utf8mb4'
            ];

            $options = [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES => false,
                PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci"
            ];

            // Intento 1: Conexión mediante IP 127.0.0.1
            try {
                $dsn1 = sprintf("mysql:host=%s;port=%d;dbname=%s;charset=%s", $db['host'], $db['port'], $db['dbname'], $db['charset']);
                self::$instance = new PDO($dsn1, $db['username'], $db['password'], $options);
            } catch (PDOException $e1) {
                // Intento 2: Fallback a 'localhost' si 127.0.0.1 falla en XAMPP
                try {
                    $dsn2 = sprintf("mysql:host=localhost;port=%d;dbname=%s;charset=%s", $db['port'], $db['dbname'], $db['charset']);
                    self::$instance = new PDO($dsn2, $db['username'], $db['password'], $options);
                } catch (PDOException $e2) {
                    error_log("Error de conexión PDO MySQL: " . $e2->getMessage());
                    throw new PDOException("Fallo al conectar a la base de datos MySQL en XAMPP. Detalle: " . $e2->getMessage(), (int)$e2->getCode());
                }
            }
        }

        return self::$instance;
    }
}
