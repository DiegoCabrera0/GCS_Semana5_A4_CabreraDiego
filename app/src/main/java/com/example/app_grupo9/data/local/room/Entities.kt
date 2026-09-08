package com.example.app_grupo9.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val rol: String,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val cedula: String
)

@Entity(tableName = "cached_grades")
data class GradeEntity(
    @PrimaryKey val id: String,
    val studentName: String,
    val materia: String,
    val profesor: String,
    val gradesJson: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "cached_tasks")
data class TaskEntity(
    @PrimaryKey val tareaId: Int,
    val titulo: String,
    val descripcion: String,
    val materia: String?,
    val estado: String,
    val fechaLimite: String
)

@Entity(tableName = "cached_announcements")
data class AnnouncementEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val contenido: String,
    val remitente: String,
    val fecha: String
)
