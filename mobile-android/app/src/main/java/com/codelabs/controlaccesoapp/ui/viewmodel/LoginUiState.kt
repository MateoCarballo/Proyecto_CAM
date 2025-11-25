package com.codelabs.controlaccesoapp.ui.viewmodel

// Este record tendra las variables que pueden modificar su estado en la pantall
// de este modo las acciones sobre la pantalla se reflejaran en ellas y realizaran cambios en la UI
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val token: String? = null,
    val errorMessage: String? = null,
)
