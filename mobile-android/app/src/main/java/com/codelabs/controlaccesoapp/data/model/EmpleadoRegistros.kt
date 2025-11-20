package com.codelabs.controlaccesoapp.data.model

data class EmpleadoRegistros (
    val idEmpleado: Integer,
    val nombreEmpleado: String,
    val numeroTarjeta: String,
    val registroPorDia: List<RegistroHorarioDia>,
)