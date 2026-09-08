<?php

namespace App\Controllers;

use App\Config\Database;
use Throwable;

class HealthController {
    public function health(): void {
        http_response_code(200);
        echo json_encode([
            'success' => true,
            'message' => 'Servicio web REST activo y escuchando peticiones.',
            'data' => [
                'status' => 'UP',
                'timestamp' => date('c')
            ]
        ]);
    }

    public function ready(): void {
        try {
            $db = Database::getConnection();
            $stmt = $db->query("SELECT 1");
            if ($stmt && $stmt->fetchColumn() == 1) {
                http_response_code(200);
                echo json_encode([
                    'success' => true,
                    'message' => 'Servicio listo y conexión a la base de datos verificada.',
                    'data' => ['database' => 'CONNECTED']
                ]);
                return;
            }
        } catch (Throwable $e) {
            error_log("Health ready check error: " . $e->getMessage());
        }

        http_response_code(503);
        echo json_encode([
            'success' => false,
            'message' => 'Servicio no disponible. Error al conectar a la base de datos.',
            'data' => null
        ]);
    }
}
