package com.codelabs.controlaccesoapp.ui.viewmodel

import com.codelabs.controlaccesoapp.data.model.DailyRecord
import com.codelabs.controlaccesoapp.data.model.DashboardUser

data class UserDashBoardUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val years: List<Int> = emptyList(),
    val selectedYear: Int? = null,

    val months: List<Int> = emptyList(),
    val selectedMonth: Int? = null,

    val filteredRecords: List<DailyRecord> = emptyList(),

    // Los datos del empleado (vienen del login)
    val dashboardUser: DashboardUser? = null
)
