<?php

namespace App\Middleware;

class RoleMiddleware {
    public static function hasRole(array $user, array $allowedRoles): bool {
        if (!isset($user['rol'])) {
            return false;
        }
        return in_array($user['rol'], $allowedRoles, true);
    }
}
