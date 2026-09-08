<?php

namespace App\Controllers;

use App\Repositories\JustificationRepository;
use App\Services\FileStorageService;
use App\Services\AuditService;

class JustificationController {
    public function getJustifications(array $currentUser): void {
        $justRepo = new JustificationRepository();

        if ($currentUser['rol'] === 'REPRESENTANTE') {
            $list = $justRepo->getJustificationsForParent($currentUser['id']);
        } else if ($currentUser['rol'] === 'ADMINISTRADOR') {
            $list = $justRepo->getPendingJustificationsForAdmin();
        } else {
            http_response_code(403);
            echo json_encode(['success' => false, 'message' => 'Acceso denegado', 'data' => null]);
            return;
        }

        http_response_code(200);
        echo json_encode(['success' => true, 'data' => $list, 'message' => 'Justificativos cargados']);
    }

    public function submitJustification(array $currentUser): void {
        if ($currentUser['rol'] !== 'REPRESENTANTE') {
            http_response_code(403);
            echo json_encode(['success' => false, 'message' => 'Solo representantes pueden enviar justificativos.', 'data' => null]);
            return;
        }

        $asistenciaId = isset($_POST['asistencia_id']) ? (int)$_POST['asistencia_id'] : null;
        $motivo = trim($_POST['motivo'] ?? '');
        $filePath = null;

        if (isset($_FILES['evidencia']) && $_FILES['evidencia']['error'] === UPLOAD_ERR_OK) {
            $storage = new FileStorageService();
            $filePath = $storage->uploadFile($_FILES['evidencia'], 'evidence');
        }

        if (!$asistenciaId || !$motivo) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'Asistencia y motivo son obligatorios.', 'data' => null]);
            return;
        }

        // Obtener ID de representante
        $db = \App\Config\Database::getConnection();
        $stmtRep = $db->prepare("SELECT id FROM representantes WHERE usuario_id = :uId LIMIT 1");
        $stmtRep->execute(['uId' => $currentUser['id']]);
        $rep = $stmtRep->fetch();

        if (!$rep) {
            http_response_code(404);
            echo json_encode(['success' => false, 'message' => 'Perfil de representante no encontrado.', 'data' => null]);
            return;
        }

        $justRepo = new JustificationRepository();
        $newId = $justRepo->createJustification($asistenciaId, $rep['id'], $motivo, $filePath);

        AuditService::log($currentUser['id'], 'SUBMIT_JUSTIFICATION', 'justificativos', $newId);

        http_response_code(201);
        echo json_encode(['success' => true, 'message' => 'Justificativo enviado correctamente para revisión.', 'data' => ['id' => $newId]]);
    }

    public function processJustification(array $currentUser): void {
        if ($currentUser['rol'] !== 'ADMINISTRADOR') {
            http_response_code(403);
            echo json_encode(['success' => false, 'message' => 'Solo administradores pueden aprobar o rechazar justificativos.', 'data' => null]);
            return;
        }

        $input = json_decode(file_get_contents('php://input'), true);
        $justificativoId = $input['justificativo_id'] ?? null;
        $estado = $input['estado'] ?? null; // 'APROBADO' o 'RECHAZADO'
        $obs = $input['observacion'] ?? null;

        if (!$justificativoId || !in_array($estado, ['APROBADO', 'RECHAZADO'], true)) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'ID y estado válidos requeridos', 'data' => null]);
            return;
        }

        $justRepo = new JustificationRepository();
        $res = $justRepo->processJustification($justificativoId, $estado, $obs, $currentUser['id']);

        if ($res) {
            AuditService::log($currentUser['id'], 'PROCESS_JUSTIFICATION', 'justificativos', $justificativoId, ['estado' => $estado]);
            http_response_code(200);
            echo json_encode(['success' => true, 'message' => "Justificativo {$estado} exitosamente.", 'data' => null]);
        } else {
            http_response_code(400);
            echo json_encode(['success' => false, 'message' => 'Error al procesar justificativo.', 'data' => null]);
        }
    }
}
