package com.example.app_grupo9.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo9.data.local.TokenManager
import com.example.app_grupo9.data.model.UserDto
import com.example.app_grupo9.data.repository.AppRepository
import com.example.app_grupo9.data.repository.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)
    private val tokenManager = TokenManager(application)

    private val _loginState = MutableStateFlow<ResultState<UserDto>?>(null)
    val loginState: StateFlow<ResultState<UserDto>?> = _loginState

    private val _currentUser = MutableStateFlow<UserDto?>(tokenManager.getUser())
    val currentUser: StateFlow<UserDto?> = _currentUser

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = ResultState.Loading
            when (val result = repository.login(username, pass)) {
                is ResultState.Success -> {
                    _currentUser.value = result.data.usuario
                    _loginState.value = ResultState.Success(result.data.usuario)
                }
                is ResultState.Error -> {
                    _loginState.value = ResultState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun logout() {
        repository.logout()
        _currentUser.value = null
        _loginState.value = null
    }
}
