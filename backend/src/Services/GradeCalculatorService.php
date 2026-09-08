<?php

namespace App\Services;

class GradeCalculatorService {
    /**
     * Calcula el promedio ponderado de un trimestre dado el mapa de componentes y sus notas.
     * $gradesMap = ['APORTES' => 9.5, 'PROYECTO' => 9.0, 'EVALUACION' => null]
     * Retorna array con 'promedio' (float|null) e 'incompleto' (bool).
     */
    public static function calculateTrimesterAverage(array $gradesMap): array {
        $weights = [
            'APORTES' => 0.40,
            'PROYECTO' => 0.30,
            'EVALUACION' => 0.30
        ];

        $totalWeightedGrade = 0.0;
        $totalWeightApplied = 0.0;
        $hasAnyPending = false;

        foreach ($weights as $code => $weight) {
            $nota = $gradesMap[$code] ?? null;
            if ($nota === null || $nota === '') {
                $hasAnyPending = true;
            } else {
                $totalWeightedGrade += ((float)$nota) * $weight;
                $totalWeightApplied += $weight;
            }
        }

        if ($totalWeightApplied == 0.0) {
            return ['promedio' => null, 'incompleto' => true];
        }

        // Si faltan notas, calculamos el promedio ponderado actual sobre los componentes rendidos
        $promedioCalculado = round($totalWeightedGrade / $totalWeightApplied, 2);

        return [
            'promedio' => $promedioCalculado,
            'incompleto' => $hasAnyPending
        ];
    }

    /**
     * Calcula el promedio final anual de los 3 trimestres.
     */
    public static function calculateAnnualAverage(array $trimesterAverages): ?float {
        $validAverages = array_filter($trimesterAverages, fn($val) => $val !== null);
        if (empty($validAverages)) {
            return null;
        }

        return round(array_sum($validAverages) / count($validAverages), 2);
    }
}
