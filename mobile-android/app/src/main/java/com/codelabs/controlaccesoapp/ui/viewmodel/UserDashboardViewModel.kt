package com.codelabs.controlaccesoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelabs.controlaccesoapp.data.model.DashboardUser
import com.codelabs.controlaccesoapp.data.model.EmployeeRecords
import com.codelabs.controlaccesoapp.data.repository.HorariosRepository
import com.codelabs.controlaccesoapp.data.repository.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UserDashboardViewModel(
    private val repository: HorariosRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserDashBoardUiState())
    val uiState: StateFlow<UserDashBoardUiState> = _uiState

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /** Carga inicial de datos desde la API */
    fun cargarDatos() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(errorMessage = "Token no disponible")
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val response = repository.getHorariosRegistros(token)
                val employee = response.getOrThrow().empleados.firstOrNull() ?: return@launch
                val dashboardUser = DashboardUser(employeeInfo = employee)
                _uiState.value = _uiState.value.copy(dashboardUser = dashboardUser)
                prepararDatos(employee)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error desconocido"
                )
            }
        }
    }

    /** Prepara años, meses y registros iniciales */
    private fun prepararDatos(employee: EmployeeRecords) {
        val fechas = employee.registrosPorDia.mapNotNull { runCatching { dateFormat.parse(it.fecha) }.getOrNull() }
        if (fechas.isEmpty()) return

        val anhos = fechas.map { Calendar.getInstance().apply { time = it }.get(Calendar.YEAR) }
            .distinct().sortedDescending()
        val anhoSeleccionado = anhos.firstOrNull() ?: return

        val meses = fechas.filter { Calendar.getInstance().apply { time = it }.get(Calendar.YEAR) == anhoSeleccionado }
            .map { Calendar.getInstance().apply { time = it }.get(Calendar.MONTH) + 1 }
            .distinct().sorted()
        val mesSeleccionado = meses.firstOrNull() ?: return

        actualizarRegistros(employee, anhoSeleccionado, mesSeleccionado, anhos, meses)
    }

    fun seleccionarAnho(anho: Int) {
        val employee = _uiState.value.dashboardUser?.employeeInfo ?: return
        val fechas = employee.registrosPorDia.mapNotNull { runCatching { dateFormat.parse(it.fecha) }.getOrNull() }
        val meses = fechas.filter { Calendar.getInstance().apply { time = it }.get(Calendar.YEAR) == anho }
            .map { Calendar.getInstance().apply { time = it }.get(Calendar.MONTH) + 1 }
            .distinct().sorted()
        val mesSeleccionado = meses.firstOrNull() ?: return
        actualizarRegistros(employee, anho, mesSeleccionado, _uiState.value.years, meses)
    }

    fun seleccionarMes(mes: Int) {
        val employee = _uiState.value.dashboardUser?.employeeInfo ?: return
        val anho = _uiState.value.selectedYear ?: return
        actualizarRegistros(employee, anho, mes, _uiState.value.years, _uiState.value.months)
    }

    private fun actualizarRegistros(
        employee: EmployeeRecords,
        anho: Int,
        mes: Int,
        anhos: List<Int>,
        meses: List<Int>
    ) {
        val registros = employee.registrosPorDia.filter { day ->
            val date = runCatching { dateFormat.parse(day.fecha) }.getOrNull() ?: return@filter false
            val cal = Calendar.getInstance().apply { time = date }
            cal.get(Calendar.YEAR) == anho && (cal.get(Calendar.MONTH) + 1) == mes
        }

        _uiState.value = _uiState.value.copy(
            years = anhos,
            selectedYear = anho,
            months = meses,
            selectedMonth = mes,
            filteredRecords = registros,
            isLoading = false
        )
    }

    /** Logout: limpia token y reinicia el estado de la UI */
    fun logout() {
        tokenManager.clearToken()
        _uiState.value = UserDashBoardUiState()
    }
}
