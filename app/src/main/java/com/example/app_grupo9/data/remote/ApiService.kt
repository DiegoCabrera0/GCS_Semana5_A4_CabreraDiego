package com.example.app_grupo9.data.remote

import com.example.app_grupo9.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @GET("me")
    suspend fun getProfile(): Response<ApiResponse<UserDto>>

    @POST("auth/change-password")
    suspend fun changePassword(@Body body: Map<String, String>): Response<ApiResponse<Unit>>

    // Users (Admin)
    @GET("users")
    suspend fun getUsers(@Query("rol") rol: String? = null): Response<ApiResponse<List<UserDto>>>

    @POST("users")
    suspend fun createUser(@Body userMap: Map<String, String>): Response<ApiResponse<Map<String, Int>>>

    @POST("users/{id}/reset-password")
    suspend fun resetPassword(@Path("id") id: Int): Response<ApiResponse<Map<String, String>>>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<ApiResponse<Unit>>

    // Grades
    @GET("grades/student")
    suspend fun getStudentGrades(@Query("alumno_id") alumnoId: Int? = null): Response<ApiResponse<StudentGradesResponse>>

    @GET("grades/matrix")
    suspend fun getGradeMatrix(
        @Query("asignacion_id") asignacionId: Int,
        @Query("trimestre_id") trimestreId: Int
    ): Response<ApiResponse<List<TeacherGradeRowDto>>>

    @POST("grades/update")
    suspend fun updateGrade(@Body body: Map<String, Any?>): Response<ApiResponse<Unit>>

    // Tasks
    @GET("tasks/student")
    suspend fun getStudentTasks(@Query("alumno_id") alumnoId: Int? = null): Response<ApiResponse<List<TaskDto>>>

    @GET("tasks/teacher")
    suspend fun getTeacherTasks(): Response<ApiResponse<List<TaskDto>>>

    @POST("tasks")
    suspend fun createTask(@Body body: Map<String, Any>): Response<ApiResponse<Map<String, Int>>>

    @Multipart
    @POST("tasks/submit")
    suspend fun submitTask(
        @Part("tarea_id") tareaId: RequestBody,
        @Part("texto_entrega") textoEntrega: RequestBody?,
        @Part archivo: MultipartBody.Part?
    ): Response<ApiResponse<Unit>>

    // Attendance
    @GET("attendance/student")
    suspend fun getStudentAttendance(@Query("alumno_id") alumnoId: Int? = null): Response<ApiResponse<AttendanceResponse>>

    @POST("attendance/mark")
    suspend fun markAttendance(@Body body: Map<String, Any>): Response<ApiResponse<Unit>>

    // Justifications
    @GET("justifications")
    suspend fun getJustifications(): Response<ApiResponse<List<JustificationDto>>>

    @Multipart
    @POST("justifications/submit")
    suspend fun submitJustification(
        @Part("asistencia_id") asistenciaId: RequestBody,
        @Part("motivo") motivo: RequestBody,
        @Part evidencia: MultipartBody.Part?
    ): Response<ApiResponse<Map<String, Int>>>

    @POST("justifications/process")
    suspend fun processJustification(@Body body: Map<String, Any?>): Response<ApiResponse<Unit>>

    // Announcements
    @GET("announcements")
    suspend fun getAnnouncements(): Response<ApiResponse<List<AnnouncementDto>>>

    @POST("announcements")
    suspend fun createAnnouncement(@Body body: Map<String, String>): Response<ApiResponse<Map<String, Int>>>

    // Notifications
    @GET("notifications")
    suspend fun getNotifications(): Response<ApiResponse<List<NotificationDto>>>

    @POST("notifications/{id}/read")
    suspend fun markNotificationRead(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
