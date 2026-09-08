<?php

/**
 * Control De Entrada Principal (Front Controller) - REST API
 * Unidad Educativa Dr. Alfredo Pareja Diezcanseco
 */

// Headers para API REST y CORS
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');

// Manejar preflight OPTIONS
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Cargar Autoloader de Composer si existe, sino autoloader PSR-4 simple
$vendorAutoload = __DIR__ . '/../vendor/autoload.php';
if (file_exists($vendorAutoload)) {
    require_once $vendorAutoload;
} else {
    spl_autoload_register(function ($class) {
        $prefix = 'App\\';
        $base_dir = __DIR__ . '/../src/';
        $len = strlen($prefix);
        if (strncmp($prefix, $class, $len) !== 0) {
            return;
        }
        $relative_class = substr($class, $len);
        $file = $base_dir . str_replace('\\', '/', $relative_class) . '.php';
        if (file_exists($file)) {
            require $file;
        }
    });
}

use App\Middleware\AuthMiddleware;
use App\Middleware\RoleMiddleware;

try {
    $routes = require __DIR__ . '/../routes/api.php';

    // Normalizar URI quitando prefijos de subcarpeta y script name
    $requestUri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
    $requestMethod = $_SERVER['REQUEST_METHOD'];

    // Eliminar prefijos comunes de subcarpeta
    $requestUri = preg_replace('#^/gestion_academica/public/index\.php#i', '', $requestUri);
    $requestUri = preg_replace('#^/gestion_academica/index\.php#i', '', $requestUri);
    $requestUri = preg_replace('#^/gestion_academica/public#i', '', $requestUri);
    $requestUri = preg_replace('#^/gestion_academica#i', '', $requestUri);
    $requestUri = preg_replace('#^/api/v1#i', '', $requestUri);

    if (empty($requestUri) || $requestUri === '/' || $requestUri === '/index.php') {
        $requestUri = '/health';
    }

    $matchedRoute = null;
    $routeParams = [];

    foreach ($routes as $route) {
        [$method, $pattern, $handler, $requiresAuth, $roles] = $route;

        if ($method !== $requestMethod) {
            continue;
        }

        // Convertir {id} a expresión regular
        $regex = '#^' . preg_replace('#\{([a-zA-Z0-9_]+)\}#', '(?P<$1>[^/]+)', $pattern) . '$#';

        if (preg_match($regex, $requestUri, $matches)) {
            $matchedRoute = $route;
            foreach ($matches as $key => $val) {
                if (is_string($key)) {
                    $routeParams[$key] = $val;
                }
            }
            break;
        }
    }

    if (!$matchedRoute) {
        http_response_code(404);
        echo json_encode([
            'success' => false,
            'message' => "Ruta no encontrada: {$requestMethod} {$requestUri}",
            'data' => null
        ]);
        exit();
    }

    [$method, $pattern, $handler, $requiresAuth, $roles] = $matchedRoute;

    // Autenticación si se requiere
    $currentUser = null;
    if ($requiresAuth) {
        $currentUser = AuthMiddleware::authenticate();
        if (!$currentUser) {
            http_response_code(401);
            echo json_encode([
                'success' => false,
                'message' => 'No autorizado. Token inexistente, inválido o expirado.',
                'data' => null
            ]);
            exit();
        }

        // Autorización de rol
        if (!empty($roles) && !RoleMiddleware::hasRole($currentUser, $roles)) {
            http_response_code(403);
            echo json_encode([
                'success' => false,
                'message' => 'Acceso prohibido. Permisos insuficientes para el rol ' . ($currentUser['rol'] ?? 'desconocido'),
                'data' => null
            ]);
            exit();
        }
    }

    // Ejecutar controlador
    [$controllerClass, $action] = explode('@', $handler);
    $controller = new $controllerClass();

    if (!empty($routeParams)) {
        $paramValues = array_values($routeParams);
        if ($requiresAuth) {
            $paramValues[] = $currentUser;
        }
        call_user_func_array([$controller, $action], $paramValues);
    } else {
        if ($requiresAuth) {
            $controller->$action($currentUser);
        } else {
            $controller->$action();
        }
    }

} catch (Throwable $e) {
    error_log("Error no capturado en API: " . $e->getMessage() . "\n" . $e->getTraceAsString());
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Error interno del servidor: ' . $e->getMessage(),
        'data' => null
    ]);
}
