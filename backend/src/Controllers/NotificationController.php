<?php

namespace App\Controllers;

use App\Repositories\NotificationRepository;

class NotificationController {
    public function index(array $currentUser): void {
        $notifRepo = new NotificationRepository();
        $list = $notifRepo->getNotificationsForUser($currentUser['id']);

        http_response_code(200);
        echo json_encode(['success' => true, 'data' => $list, 'message' => 'Notificaciones obtenidas']);
    }

    public function markRead(int $id, array $currentUser): void {
        $notifRepo = new NotificationRepository();
        $notifRepo->markAsRead($id, $currentUser['id']);

        http_response_code(200);
        echo json_encode(['success' => true, 'message' => 'Notificación marcada como leída', 'data' => null]);
    }
}
