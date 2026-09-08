<?php
/**
 * Conexión PDO a MySQL / MariaDB en XAMPP
 */

namespace App\Config;

use PDO;
use PDOException;

class Database {
    private static ?PDO $instance = null;

    public static function getConnection(): PDO {
        if (self::$instance === null) {
            $config = require __DIR__ . '/config.php';
            $db = $config['database'];

            $dsn = sprintf(
                "mysql:host=%s;port=%d;dbname=%s;charset=%s",
                $db['host'],
                $db['port'],
                $db['dbname'],
                $db['charset']
            );

            $options = [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES => false,
                PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci"
            ];

            try {
                self::$instance = new PDO($dsn, $db['username'], $db['password'], $options);
            } catch (PDOException $e) {
                // Registrar log interno
                error_log("Error de conexión a la base de datos: " . $e->getMessage());
                throw new PDOException("Error al conectar con la base de datos.", (int)$e->getCode());
            }
        }

        return self::$instance;
    }
}
