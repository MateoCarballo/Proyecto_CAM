package com.codelabs.controlaccesoapp.data.model

data class RegisterRequest(
    val empleadoId: String,
    val email: String,
    val password: String,
)