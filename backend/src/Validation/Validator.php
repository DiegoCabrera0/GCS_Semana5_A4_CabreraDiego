<?php

namespace App\Validation;

class Validator {
    private array $errors = [];

    public function required(array $data, array $fields): self {
        foreach ($fields as $field) {
            if (!isset($data[$field]) || trim((string)$data[$field]) === '') {
                $this->errors[$field][] = "El campo '{$field}' es obligatorio.";
            }
        }
        return $this;
    }

    public function numericRange(string $field, $value, float $min, float $max): self {
        if ($value !== null && ($value < $min || $value > $max)) {
            $this->errors[$field][] = "El campo '{$field}' debe estar entre {$min} y {$max}.";
        }
        return $this;
    }

    public function cedulaEcuador(string $field, string $cedula): self {
        if (!preg_match('/^[0-9]{10}$/', $cedula)) {
            $this->errors[$field][] = "La cédula debe contener exactamente 10 dígitos numéricos.";
        }
        return $this;
    }

    public function hasErrors(): bool {
        return !empty($this->errors);
    }

    public function getErrors(): array {
        return $this->errors;
    }
}
