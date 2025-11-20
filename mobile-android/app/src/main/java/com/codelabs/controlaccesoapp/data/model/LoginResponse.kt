package com.codelabs.controlaccesoapp.data.model

data class LoginResponse(
    val token: String,
    val tipo: String,
    val mensaje: String,
)