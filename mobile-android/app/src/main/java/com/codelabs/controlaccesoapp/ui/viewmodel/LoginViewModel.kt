package com.codelabs.controlaccesoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelabs.controlaccesoapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // Con esto hacemos que la parte inmutable pueda
    // ser visualizada desde la pantalla y solo modificada desde aquí
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
        )
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
        )
    }

    fun login() {

        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank()){
            _uiState.value = _uiState.value.copy(
                errorMessage = "El email no puede estar vacío"
            )
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _uiState.value = _uiState.value.copy(
                errorMessage = "El email no es válido"
            )
        }

        if (password.length < 6){
            _uiState.value = _uiState.value.copy(
                errorMessage = "La contraseña debe tener al menos 6 caracteres"
            )
        }

        viewModelScope.launch {

            //Indicamos que estamos cargando
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
            )

            val result = repository.login(
                _uiState.value.email,
                _uiState.value.password,
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
            )

            //Procesar los resultados
            // TODO como almaceno esto para no
            //  perderlo si cierro y abro la app quiero poder tener
            //  la sesion iniciada. Quiza es algo demasiado tedioso y complicado
            result.onSuccess { response ->
                _uiState.value = _uiState.value.copy(
                    isLoginSuccess = true,
                    token = response.token
                )
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = throwable.message,
                )
            }

        }
    }
}