<?php

namespace App\Controllers;

use App\Repositories\TaskRepository;
use App\Repositories\StudentRepository;
use App\Services\FileStorageService;
use App\Services\AuditService;

class TaskController {
    public function getStudentTasks(array $currentUser): void {
        $studentRepo = new StudentRepository();
        $student = null;

        if ($currentUser['rol'] === 'ALUMNO') {
            $student = $studentRepo->findByUserId($currentUser['id']);
        } else if ($currentUser['rol'] === 'REPRESENTANTE') {
            $selectedStudentId = isset($_GET['alumno_id']) ? (int)$_GET['alumno_id'] : null;
            $representeds = $studentRepo->getStudentsByParentUserId($currentUser['id']);
            if (!empty($representeds)) {
                $student = $selectedStudentId ? array_filter($representeds, fn($s) => $s['alumno_id'] === $selectedStudentId)[0] ?? $representeds[0] : $representeds[0];
            }
        }

        if (!$student) {
            http_response_code(404);
            echo json_encode(['success' => false, 'message' => 'Estudiante no encontrado', 'data' => null]);
            return;
        }

        $taskRepo = new TaskRepository();
        $tasks = $taskRepo->getTasksForStudent($student['alumno_id']);

        http_response_code(200);
        echo json_encode(['success' => true, 'data' => $tasks, 'message' => 'Tareas obtenidas']);
    }

    public function getTeacherTasks(array $currentUser): void {
        $taskRepo = new TaskRepository();
        $tasks = $taskRepo->getTasksForTeacher($currentUser['id']);

        http_response_code(200);
        echo json_encode(['success' => true, 'data' => $tasks, 'message' => 'Tareas del docente obtenidas']);
    }

    public function createTask(array $currentUser): void {
        $input = json_decode(file_get_contents('php://input'), true);

        if (empty($input['asignacion_id']) || empty($input['titulo']) || empty($input['descripcion']) || empty($input['fecha_limite'])) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'Campos obligatorios requeridos', 'data' => null]);
            return;
        }

        $taskRepo = new TaskRepository();
        $tareaId = $taskRepo->createTask([
            'asignacion_id' => (int)$input['asignacion_id'],
            'trimestre_id' => $input['trimestre_id'] ?? 2,
            'titulo' => trim($input['titulo']),
            'descripcion' => trim($input['descripcion']),
            'tipo' => $input['tipo'] ?? 'DEBER',
            'fecha_limite' => $input['fecha_limite'],
            'archivo_adjunto' => $input['archivo_adjunto'] ?? null
        ]);

        AuditService::log($currentUser['id'], 'CREATE_TASK', 'tareas', $tareaId);

        http_response_code(201);
        echo json_encode(['success' => true, 'message' => 'Tarea publicada exitosamente', 'data' => ['id' => $tareaId]]);
    }

    public function submitTask(array $currentUser): void {
        $studentRepo = new StudentRepository();
        $student = $studentRepo->findByUserId($currentUser['id']);

        if (!$student) {
            http_response_code(403);
            echo json_encode(['success' => false, 'message' => 'Solo estudiantes pueden entregar tareas.', 'data' => null]);
            return;
        }

        $tareaId = isset($_POST['tarea_id']) ? (int)$_POST['tarea_id'] : null;
        $texto = $_POST['texto_entrega'] ?? null;
        $filePath = null;

        if (isset($_FILES['archivo']) && $_FILES['archivo']['error'] === UPLOAD_ERR_OK) {
            $storage = new FileStorageService();
            $filePath = $storage->uploadFile($_FILES['archivo'], 'tasks');
        }

        if (!$tareaId) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'ID de tarea requerido', 'data' => null]);
            return;
        }

        $taskRepo = new TaskRepository();
        $taskRepo->submitTask($tareaId, $student['alumno_id'], $texto, $filePath);

        AuditService::log($currentUser['id'], 'SUBMIT_TASK', 'entregas_tareas', null, ['tarea_id' => $tareaId]);

        http_response_code(200);
        echo json_encode(['success' => true, 'message' => 'Tarea entregada exitosamente', 'data' => null]);
    }
}
