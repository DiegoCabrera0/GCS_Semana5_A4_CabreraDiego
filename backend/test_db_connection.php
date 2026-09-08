<?php
/**
 * Script de prueba de conexión Backend PHP -> MySQL Database
 */

require_once __DIR__ . '/config/database.php';

use App\Config\Database;

echo "--- INICIANDO VERIFICACIÓN DE CONEXIÓN ---\n";

try {
    $db = Database::getConnection();
    echo "[OK] Conexión PDO establecida con éxito a MySQL/MariaDB.\n\n";

    // 1. Probar consulta a la tabla institucion
    $stmtInst = $db->query("SELECT nombre, codigo_amie, ciudad FROM institucion LIMIT 1");
    $inst = $stmtInst->fetch();
    if ($inst) {
        echo "[OK] Institución encontrada: {$inst['nombre']} (AMIE: {$inst['codigo_amie']}, {$inst['ciudad']})\n";
    } else {
        echo "[AVISO] La tabla 'institucion' existe pero está vacía. Se requiere ejecutar seed.sql.\n";
    }

    // 2. Contar usuarios por rol
    $stmtUsers = $db->query("SELECT rol, COUNT(*) as cantidad FROM usuarios GROUP BY rol");
    $roles = $stmtUsers->fetchAll();
    echo "\n[OK] Conteo de usuarios registrados por rol:\n";
    if (!empty($roles)) {
        foreach ($roles as $row) {
            echo " - {$row['rol']}: {$row['cantidad']} usuario(s)\n";
        }
    } else {
        echo "[AVISO] No hay usuarios en la tabla 'usuarios'. Se requiere ejecutar seed.sql.\n";
    }

    // 3. Probar usuario admin
    $stmtAdmin = $db->query("SELECT username, rol, nombres, apellidos FROM usuarios WHERE username = 'admin' LIMIT 1");
    $admin = $stmtAdmin->fetch();
    if ($admin) {
        echo "\n[OK] Usuario 'admin' verificado: {$admin['nombres']} {$admin['apellidos']} ({$admin['rol']})\n";
    } else {
        echo "\n[AVISO] Usuario 'admin' no encontrado en la base de datos.\n";
    }

    echo "\n--- VERIFICACIÓN DE BASE DE DATOS COMPLETADA EXITOSAMENTE ---\n";

} catch (Throwable $e) {
    echo "[ERROR] Fallo de conexión o consulta SQL: " . $e->getMessage() . "\n";
    echo "Detalles de solución:\n";
    echo " 1. Asegúrese de que el servicio MySQL en XAMPP esté iniciado (puerto 3306).\n";
    echo " 2. Verifique que la base de datos 'gestion_academica_ueapd' haya sido creada.\n";
    echo " 3. Ejecute schema.sql y seed.sql en phpMyAdmin o MySQL CLI.\n";
}
