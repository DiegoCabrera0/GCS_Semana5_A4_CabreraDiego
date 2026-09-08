<?php

namespace App\Controllers;

use App\Repositories\AttendanceRepository;
use App\Repositories\StudentRepository;
use App\Services\AuditService;

class AttendanceController {
    public function getStudentAttendance(array $currentUser): void {
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

        $attRepo = new AttendanceRepository();
        $summary = $attRepo->getAttendanceSummaryForStudent($student['alumno_id']);
        $records = $attRepo->getAttendanceRecordsForStudent($student['alumno_id']);

        http_response_code(200);
        echo json_encode([
            'success' => true,
            'message' => 'Asistencia obtenida',
            'data' => [
                'estudiante' => $student,
                'resumen' => $summary,
                'registros' => $records
            ]
        ]);
    }

    public function markAttendance(array $currentUser): void {
        $input = json_decode(file_get_contents('php://input'), true);

        if (empty($input['alumno_id']) || empty($input['paralelo_id']) || empty($input['fecha']) || empty($input['estado'])) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'Parámetros obligatorios de asistencia incompletos', 'data' => null]);
            return;
        }

        $attRepo = new AttendanceRepository();
        $attRepo->upsertAttendanceRecord(
            (int)$input['alumno_id'],
            (int)$input['paralelo_id'],
            $input['trimestre_id'] ?? 2,
            $input['fecha'],
            $input['estado'],
            $input['observacion'] ?? null,
            $currentUser['id']
        );

        AuditService::log($currentUser['id'], 'MARK_ATTENDANCE', 'asistencia', null, [
            'alumno_id' => $input['alumno_id'], 'fecha' => $input['fecha'], 'estado' => $input['estado']
        ]);

        http_response_code(200);
        echo json_encode(['success' => true, 'message' => 'Asistencia registrada', 'data' => null]);
    }
}
