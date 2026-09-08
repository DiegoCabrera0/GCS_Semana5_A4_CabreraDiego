<?php

namespace App\Services;

class FileStorageService {
    private string $storageDir;
    private array $allowedExtensions;
    private int $maxSize;

    public function __construct() {
        $config = require __DIR__ . '/../../config/config.php';
        $this->storageDir = $config['upload']['private_dir'];
        $this->allowedExtensions = $config['upload']['allowed_extensions'];
        $this->maxSize = $config['upload']['max_file_size'];

        if (!is_dir($this->storageDir)) {
            @mkdir($this->storageDir, 0755, true);
        }
    }

    public function uploadFile(array $file, string $subFolder = 'evidence'): ?string {
        if ($file['error'] !== UPLOAD_ERR_OK) {
            return null;
        }

        if ($file['size'] > $this->maxSize) {
            return null;
        }

        $extension = strtolower(pathinfo($file['name'], PATHINFO_EXTENSION));
        if (!in_array($extension, $this->allowedExtensions, true)) {
            return null;
        }

        $targetDir = $this->storageDir . '/' . $subFolder;
        if (!is_dir($targetDir)) {
            @mkdir($targetDir, 0755, true);
        }

        $filename = sprintf('%s_%s.%s', date('Ymd_His'), bin2hex(random_bytes(8)), $extension);
        $targetPath = $targetDir . '/' . $filename;

        if (move_uploaded_file($file['tmp_name'], $targetPath)) {
            return $subFolder . '/' . $filename;
        }

        return null;
    }

    public function getAbsolutePath(string $relativePath): ?string {
        $fullPath = realpath($this->storageDir . '/' . $relativePath);
        if ($fullPath && str_starts_with($fullPath, realpath($this->storageDir))) {
            return $fullPath;
        }
        return null;
    }
}
