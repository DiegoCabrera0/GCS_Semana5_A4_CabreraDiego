<?php

/**
 * Definición de Rutas API REST
 * Unidad Educativa Dr. Alfredo Pareja Diezcanseco
 */

return [
    // Monitoreo
    ['GET', '/health', 'App\Controllers\HealthController@health', false, []],
    ['GET', '/ready', 'App\Controllers\HealthController@ready', false, []],

    // Autenticación
    ['POST', '/auth/login', 'App\Controllers\AuthController@login', false, []],
    ['GET', '/me', 'App\Controllers\AuthController@me', true, []],
    ['POST', '/auth/change-password', 'App\Controllers\AuthController@changePassword', true, []],

    // Usuarios (Admin)
    ['GET', '/users', 'App\Controllers\UserController@index', true, ['ADMINISTRADOR']],
    ['POST', '/users', 'App\Controllers\UserController@store', true, ['ADMINISTRADOR']],
    ['POST', '/users/{id}/reset-password', 'App\Controllers\UserController@resetPassword', true, ['ADMINISTRADOR']],
    ['DELETE', '/users/{id}', 'App\Controllers\UserController@delete', true, ['ADMINISTRADOR']],

    // Calificaciones
    ['GET', '/grades/student', 'App\Controllers\GradeController@getStudentGrades', true, ['ALUMNO', 'REPRESENTANTE']],
    ['GET', '/grades/matrix', 'App\Controllers\GradeController@getGradeMatrix', true, ['PROFESOR', 'ADMINISTRADOR']],
    ['POST', '/grades/update', 'App\Controllers\GradeController@updateGrade', true, ['PROFESOR', 'ADMINISTRADOR']],

    // Tareas
    ['GET', '/tasks/student', 'App\Controllers\TaskController@getStudentTasks', true, ['ALUMNO', 'REPRESENTANTE']],
    ['GET', '/tasks/teacher', 'App\Controllers\TaskController@getTeacherTasks', true, ['PROFESOR']],
    ['POST', '/tasks', 'App\Controllers\TaskController@createTask', true, ['PROFESOR']],
    ['POST', '/tasks/submit', 'App\Controllers\TaskController@submitTask', true, ['ALUMNO']],

    // Asistencia
    ['GET', '/attendance/student', 'App\Controllers\AttendanceController@getStudentAttendance', true, ['ALUMNO', 'REPRESENTANTE']],
    ['POST', '/attendance/mark', 'App\Controllers\AttendanceController@markAttendance', true, ['PROFESOR', 'ADMINISTRADOR']],

    // Justificativos
    ['GET', '/justifications', 'App\Controllers\JustificationController@getJustifications', true, ['REPRESENTANTE', 'ADMINISTRADOR']],
    ['POST', '/justifications/submit', 'App\Controllers\JustificationController@submitJustification', true, ['REPRESENTANTE']],
    ['POST', '/justifications/process', 'App\Controllers\JustificationController@processJustification', true, ['ADMINISTRADOR']],

    // Comunicados
    ['GET', '/announcements', 'App\Controllers\AnnouncementController@index', true, []],
    ['POST', '/announcements', 'App\Controllers\AnnouncementController@store', true, ['ADMINISTRADOR', 'PROFESOR']],

    // Notificaciones
    ['GET', '/notifications', 'App\Controllers\NotificationController@index', true, []],
    ['POST', '/notifications/{id}/read', 'App\Controllers\NotificationController@markRead', true, []],
];
