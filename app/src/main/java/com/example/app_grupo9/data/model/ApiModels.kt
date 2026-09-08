package com.example.app_grupo9.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
    val errors: Map<String, List<String>>? = null
)

enum class UserRole {
    ADMINISTRADOR,
    PROFESOR,
    ALUMNO,
    REPRESENTANTE
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val usuario: UserDto
)

data class UserDto(
    val id: Int,
    val username: String,
    val rol: String,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val cedula: String,
    @SerializedName("debe_cambiar_pass") val debeCambiarPass: Boolean = false
)

data class StudentDto(
    @SerializedName("alumno_id") val alumnoId: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    val nombres: String,
    val apellidos: String,
    val cedula: String,
    @SerializedName("codigo_estudiantil") val codigoEstudiantil: String,
    val curso: String? = null,
    val paralelo: String? = null,
    val periodo: String? = null
)

data class StudentGradesResponse(
    val estudiante: StudentDto,
    val materias: List<SubjectGradesDto>
)

data class SubjectGradesDto(
    @SerializedName("materia_id") val materiaId: Int,
    val materia: String,
    val profesor: String,
    val trimestres: Map<String, TrimesterGradesDto>,
    @SerializedName("promedio_final") val promedioFinal: Double? = null
)

data class TrimesterGradesDto(
    val aportes: Double? = null,
    val proyecto: Double? = null,
    val evaluacion: Double? = null,
    val promedio: Double? = null
)

data class TaskDto(
    @SerializedName("tarea_id") val tareaId: Int,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    @SerializedName("fecha_limite") val fechaLimite: String,
    @SerializedName("archivo_adjunto") val archivoAdjunto: String? = null,
    val materia: String? = null,
    val estado: String = "PENDIENTE",
    val nota: Double? = null,
    val retroalimentacion: String? = null
)

data class AttendanceSummaryDto(
    @SerializedName("total_dias") val totalDias: Int,
    val presentes: Int,
    val atrasos: Int,
    @SerializedName("faltas_injustificadas") val faltasInjustificadas: Int,
    @SerializedName("faltas_justificadas") val faltasJustificadas: Int
)

data class AttendanceRecordDto(
    val id: Int,
    val fecha: String,
    val estado: String,
    val observacion: String? = null,
    @SerializedName("trimestre_nombre") val trimestreNombre: String? = null
)

data class AttendanceResponse(
    val estudiante: StudentDto,
    val resumen: AttendanceSummaryDto,
    val registros: List<AttendanceRecordDto>
)

data class JustificationDto(
    @SerializedName("justificativo_id") val justificativoId: Int,
    val motivo: String,
    @SerializedName("archivo_evidencia") val archivoEvidencia: String? = null,
    val estado: String,
    @SerializedName("observacion_admin") val observacionAdmin: String? = null,
    @SerializedName("fecha_envio") val fechaEnvio: String,
    @SerializedName("fecha_asistencia") val fechaAsistencia: String? = null,
    @SerializedName("alumno_nombres") val alumnoNombres: String? = null,
    @SerializedName("alumno_apellidos") val alumnoApellidos: String? = null,
    @SerializedName("rep_nombres") val repNombres: String? = null,
    @SerializedName("rep_apellidos") val repApellidos: String? = null
)

data class AnnouncementDto(
    val id: Int,
    val titulo: String,
    val contenido: String,
    @SerializedName("dirigido_a_rol") val dirigidoARol: String,
    @SerializedName("fecha_publicacion") val fechaPublicacion: String,
    @SerializedName("remitente_nombres") val remitenteNombres: String? = null,
    @SerializedName("remitente_apellidos") val remitenteApellidos: String? = null
)

data class NotificationDto(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val tipo: String,
    val leido: Boolean,
    @SerializedName("fecha_creacion") val fechaCreacion: String
)

data class TeacherGradeRowDto(
    @SerializedName("alumno_id") val alumnoId: Int,
    val nombres: String,
    val apellidos: String,
    @SerializedName("codigo_estudiantil") val codigoEstudiantil: String,
    @SerializedName("nota_aportes") val notaAportes: Double? = null,
    @SerializedName("nota_proyecto") val notaProyecto: Double? = null,
    @SerializedName("nota_evaluacion") val notaEvaluacion: Double? = null
)
