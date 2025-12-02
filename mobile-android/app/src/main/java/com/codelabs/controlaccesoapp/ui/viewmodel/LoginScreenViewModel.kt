package com.codelabs.controlaccesoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelabs.controlaccesoapp.data.repository.AuthRepository
import com.codelabs.controlaccesoapp.data.repository.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState: StateFlow<LoginScreenUiState> = _uiState

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun login() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        // Validaciones locales
        when {
            email.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "El email no puede estar vacío")
                return
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "El email no es válido")
                return
            }
            password.length < 6 -> {
                _uiState.value = _uiState.value.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.login(email, password)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess { response ->
                tokenManager.saveToken(response.token)
                _uiState.value = _uiState.value.copy(isLoginSuccess = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Error desconocido")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginScreenUiState()
    }

    /** Limpia el error después de mostrarlo en Snackbar */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
