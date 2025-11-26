package com.codelabs.controlaccesoapp.data.model

data class EmpleadoRegistros (
    val idEmpleado: Int,
    val nombreEmpleado: String,
    val numeroTarjeta: String,
    val registrosPorDia: List<RegistroHorarioDia>,
)