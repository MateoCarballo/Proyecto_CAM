package com.codelabs.controlaccesoapp.data.model

data class LoginResponse(
    val token: String,
    val type: String,
    val message: String,
)