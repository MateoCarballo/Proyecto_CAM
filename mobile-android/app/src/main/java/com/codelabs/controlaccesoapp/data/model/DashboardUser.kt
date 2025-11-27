package com.codelabs.controlaccesoapp.data.model

data class DashboardUser(
    val employeeInfo: EmployeeRecords,
    val years: List<Int> = emptyList(),
    val selectedYear: Int? = null,
    val months: List<Int> = emptyList(),
    val selectedMonth: Int? = null,
    val filteredRecords: List<DailyRecord> = emptyList()
)