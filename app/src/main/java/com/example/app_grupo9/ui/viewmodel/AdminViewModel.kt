package com.example.app_grupo9.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo9.data.model.JustificationDto
import com.example.app_grupo9.data.model.UserDto
import com.example.app_grupo9.data.repository.AppRepository
import com.example.app_grupo9.data.repository.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)

    private val _usersState = MutableStateFlow<ResultState<List<UserDto>>>(ResultState.Loading)
    val usersState: StateFlow<ResultState<List<UserDto>>> = _usersState

    private val _justificationsState = MutableStateFlow<ResultState<List<JustificationDto>>>(ResultState.Loading)
    val justificationsState: StateFlow<ResultState<List<JustificationDto>>> = _justificationsState

    private val _actionResult = MutableStateFlow<ResultState<Unit>?>(null)
    val actionResult: StateFlow<ResultState<Unit>?> = _actionResult

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _usersState.value = repository.getUsers()
            _justificationsState.value = repository.getJustifications()
        }
    }

    fun processJustification(id: Int, estado: String, obs: String?) {
        viewModelScope.launch {
            _actionResult.value = ResultState.Loading
            val res = repository.processJustification(id, estado, obs)
            _actionResult.value = res
            if (res is ResultState.Success) {
                loadData()
            }
        }
    }
}
