package com.codelabs.controlaccesoapp.data.model

data class EmployeeRecords(
    val idEmpleado: Int,
    val nombreEmpleado: String,
    val numeroTarjeta: String,
    val registrosPorDia: List<DailyRecord>
)