<?php

namespace App\Controllers;

use App\Repositories\GradeRepository;
use App\Repositories\StudentRepository;
use App\Services\GradeCalculatorService;
use App\Services\AuditService;

class GradeController {
    public function getStudentGrades(array $currentUser): void {
        $studentRepo = new StudentRepository();
        $student = null;

        if ($currentUser['rol'] === 'ALUMNO') {
            $student = $studentRepo->findByUserId($currentUser['id']);
        } else if ($currentUser['rol'] === 'REPRESENTANTE') {
            $selectedStudentId = isset($_GET['alumno_id']) ? (int)$_GET['alumno_id'] : null;
            $representeds = $studentRepo->getStudentsByParentUserId($currentUser['id']);

            if (empty($representeds)) {
                http_response_code(404);
                echo json_encode(['success' => false, 'message' => 'No tiene estudiantes asociados.', 'data' => null]);
                return;
            }

            if ($selectedStudentId) {
                foreach ($representeds as $rep) {
                    if ($rep['alumno_id'] === $selectedStudentId) {
                        $student = $rep;
                        break;
                    }
                }
            }
            if (!$student) {
                $student = $representeds[0]; // Seleccionar primero por defecto
            }
        }

        if (!$student) {
            http_response_code(404);
            echo json_encode(['success' => false, 'message' => 'Estudiante no encontrado.', 'data' => null]);
            return;
        }

        $gradeRepo = new GradeRepository();
        $rawGrades = $gradeRepo->getGradesForStudent($student['alumno_id']);

        // Agrupar por Materia -> Trimestre
        $grouped = [];
        foreach ($rawGrades as $row) {
            $matId = $row['materia_id'];
            $trimNum = $row['trimestre_num'];

            if (!isset($grouped[$matId])) {
                $grouped[$matId] = [
                    'materia_id' => $matId,
                    'materia' => $row['materia'],
                    'profesor' => $row['profesor_nombres'] . ' ' . $row['profesor_apellidos'],
                    'trimestres' => [
                        1 => ['aportes' => null, 'proyecto' => null, 'evaluacion' => null, 'promedio' => null],
                        2 => ['aportes' => null, 'proyecto' => null, 'evaluacion' => null, 'promedio' => null],
                        3 => ['aportes' => null, 'proyecto' => null, 'evaluacion' => null, 'promedio' => null]
                    ],
                    'promedio_final' => null
                ];
            }

            $compCode = strtolower($row['componente_codigo']);
            if (isset($grouped[$matId]['trimestres'][$trimNum])) {
                $grouped[$matId]['trimestres'][$trimNum][$compCode] = $row['nota'] !== null ? (float)$row['nota'] : null;
            }
        }

        // Calcular promedios trimestrales y finales
        foreach ($grouped as &$mat) {
            $trimAverages = [];
            foreach ($mat['trimestres'] as $num => &$tData) {
                $calc = GradeCalculatorService::calculateTrimesterAverage([
                    'APORTES' => $tData['aportes'],
                    'PROYECTO' => $tData['proyecto'],
                    'EVALUACION' => $tData['evaluacion']
                ]);
                $tData['promedio'] = $calc['promedio'];
                $trimAverages[] = $calc['promedio'];
            }
            $mat['promedio_final'] = GradeCalculatorService::calculateAnnualAverage($trimAverages);
        }

        http_response_code(200);
        echo json_encode([
            'success' => true,
            'message' => 'Calificaciones obtenidas',
            'data' => [
                'estudiante' => $student,
                'materias' => array_values($grouped)
            ]
        ]);
    }

    public function getGradeMatrix(array $currentUser): void {
        $asignacionId = isset($_GET['asignacion_id']) ? (int)$_GET['asignacion_id'] : 0;
        $trimestreId = isset($_GET['trimestre_id']) ? (int)$_GET['trimestre_id'] : 2;

        if (!$asignacionId) {
            http_response_code(400);
            echo json_encode(['success' => false, 'message' => 'Asignación requerida', 'data' => null]);
            return;
        }

        $gradeRepo = new GradeRepository();
        $matrix = $gradeRepo->getGradesMatrixForTeacher($asignacionId, $trimestreId);

        http_response_code(200);
        echo json_encode(['success' => true, 'data' => $matrix, 'message' => 'Matriz de calificaciones cargada']);
    }

    public function updateGrade(array $currentUser): void {
        $input = json_decode(file_get_contents('php://input'), true);

        $alumnoId = $input['alumno_id'] ?? null;
        $asignacionId = $input['asignacion_id'] ?? null;
        $trimestreId = $input['trimestre_id'] ?? null;
        $componenteId = $input['componente_id'] ?? null;
        $nota = isset($input['nota']) && $input['nota'] !== '' ? (float)$input['nota'] : null;
        $obs = $input['observacion'] ?? null;

        if (!$alumnoId || !$asignacionId || !$trimestreId || !$componenteId) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'Parámetros obligatorios faltantes', 'data' => null]);
            return;
        }

        if ($nota !== null && ($nota < 0.0 || $nota > 10.0)) {
            http_response_code(422);
            echo json_encode(['success' => false, 'message' => 'La nota debe estar entre 0.00 y 10.00', 'data' => null]);
            return;
        }

        $gradeRepo = new GradeRepository();
        $gradeRepo->upsertGrade($alumnoId, $asignacionId, $trimestreId, $componenteId, $nota, $obs, $currentUser['id']);

        AuditService::log($currentUser['id'], 'UPDATE_GRADE', 'calificaciones', null, [
            'alumno_id' => $alumnoId, 'asignacion_id' => $asignacionId, 'nota' => $nota
        ]);

        http_response_code(200);
        echo json_encode(['success' => true, 'message' => 'Calificación guardada correctamente', 'data' => null]);
    }
}
