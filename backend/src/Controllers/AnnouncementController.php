<?php

namespace App\Controllers;

use App\Repositories\AnnouncementRepository;
use App\Services\AuditService;

class AnnouncementController {
    public function index(array $currentUser): void {
        $annRepo = new AnnouncementRepository();
        $list = $annRepo->getAnnouncementsForUser($currentUser['rol']);

        http_response_code(200);
        echo json_encode(['success' => true, 'data' => $list, 'message' => 'Comunicados obtenidos']);
    }

    public function store(array $currentUser): void {
        if (!in_array($currentUser['rol'], ['ADMINISTRADOR', 'PROFESOR'], true)) {
            http_response_code(403);
            echo json_encode(['success' => false, 'message' => 'No autorizado para publicar comunicados', 'data' => null]);
            return;
        }

        $input = json_decode(file_get_contents('php://input'), true);

        if (empty($input['titulo']) || empty($input['contenido'])) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'Título y contenido requeridos', 'data' => null]);
            return;
        }

        $annRepo = new AnnouncementRepository();
        $id = $annRepo->createAnnouncement([
            'titulo' => trim($input['titulo']),
            'contenido' => trim($input['contenido']),
            'remitente_id' => $currentUser['id'],
            'dirigido_a_rol' => $input['dirigido_a_rol'] ?? 'TODOS',
            'paralelo_id' => $input['paralelo_id'] ?? null
        ]);

        AuditService::log($currentUser['id'], 'CREATE_ANNOUNCEMENT', 'comunicados', $id);

        http_response_code(201);
        echo json_encode(['success' => true, 'message' => 'Comunicado publicado exitosamente', 'data' => ['id' => $id]]);
    }
}
