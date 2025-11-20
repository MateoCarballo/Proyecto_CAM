package com.codelabs.controlaccesoapp.data.model

data class RegistroHorariosResponse(
    val rol: String,
    val empleados: List<EmpleadoRegistros>,
)