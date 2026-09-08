package com.example.app_grupo9.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo9.data.model.*
import com.example.app_grupo9.data.repository.AppRepository
import com.example.app_grupo9.data.repository.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)

    private val _gradesState = MutableStateFlow<ResultState<StudentGradesResponse>>(ResultState.Loading)
    val gradesState: StateFlow<ResultState<StudentGradesResponse>> = _gradesState

    private val _tasksState = MutableStateFlow<ResultState<List<TaskDto>>>(ResultState.Loading)
    val tasksState: StateFlow<ResultState<List<TaskDto>>> = _tasksState

    private val _attendanceState = MutableStateFlow<ResultState<AttendanceResponse>>(ResultState.Loading)
    val attendanceState: StateFlow<ResultState<AttendanceResponse>> = _attendanceState

    private val _announcementsState = MutableStateFlow<ResultState<List<AnnouncementDto>>>(ResultState.Loading)
    val announcementsState: StateFlow<ResultState<List<AnnouncementDto>>> = _announcementsState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _gradesState.value = repository.getStudentGrades()
            _tasksState.value = repository.getStudentTasks()
            _attendanceState.value = repository.getStudentAttendance()
            _announcementsState.value = repository.getAnnouncements()
        }
    }
}
