package com.codelabs.controlaccesoapp.data.model

data class DailyRecord(
    val fecha: String,
    val horarios: List<TimeRange>
)