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

class ParentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)

    private val _selectedStudentId = MutableStateFlow<Int?>(null)
    val selectedStudentId: StateFlow<Int?> = _selectedStudentId

    private val _gradesState = MutableStateFlow<ResultState<StudentGradesResponse>>(ResultState.Loading)
    val gradesState: StateFlow<ResultState<StudentGradesResponse>> = _gradesState

    private val _attendanceState = MutableStateFlow<ResultState<AttendanceResponse>>(ResultState.Loading)
    val attendanceState: StateFlow<ResultState<AttendanceResponse>> = _attendanceState

    private val _justificationsState = MutableStateFlow<ResultState<List<JustificationDto>>>(ResultState.Loading)
    val justificationsState: StateFlow<ResultState<List<JustificationDto>>> = _justificationsState

    init {
        loadData()
    }

    fun setSelectedStudent(id: Int) {
        _selectedStudentId.value = id
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val studentId = _selectedStudentId.value
            _gradesState.value = repository.getStudentGrades(studentId)
            _attendanceState.value = repository.getStudentAttendance(studentId)
            _justificationsState.value = repository.getJustifications()
        }
    }
}
