package com.example.app_grupo9.data.local.room

import androidx.room.*

@Dao
interface AppDao {
    @Query("SELECT * FROM cached_grades")
    suspend fun getAllCachedGrades(): List<GradeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: GradeEntity)

    @Query("SELECT * FROM cached_tasks")
    suspend fun getAllCachedTasks(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Query("SELECT * FROM cached_announcements")
    suspend fun getAllCachedAnnouncements(): List<AnnouncementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<AnnouncementEntity>)

    @Query("DELETE FROM cached_grades")
    suspend fun clearGrades()

    @Query("DELETE FROM cached_tasks")
    suspend fun clearTasks()

    @Query("DELETE FROM cached_announcements")
    suspend fun clearAnnouncements()
}
