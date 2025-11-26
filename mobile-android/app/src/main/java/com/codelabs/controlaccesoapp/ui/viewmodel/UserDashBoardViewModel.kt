package com.codelabs.controlaccesoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelabs.controlaccesoapp.data.model.EmpleadoRegistros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UserDashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UserDashBoardUiState())
    val uiState: StateFlow<UserDashBoardUiState> = _uiState

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /** Cargar los datos del usuario */
    fun cargarDatos(usuario: EmpleadoRegistros) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                empleado = usuario
            )
            prepararDatos(usuario)
        }
    }

    /** Preparar años, meses y registros filtrados iniciales */
    private fun prepararDatos(empleado: EmpleadoRegistros) {
        val fechas = empleado.registrosPorDia.mapNotNull { runCatching { dateFormat.parse(it.fecha) }.getOrNull() }

        if (fechas.isEmpty()) return

        val años = fechas.map { Calendar.getInstance().apply { time = it }.get(Calendar.YEAR) }
            .distinct()
            .sortedDescending()
        val añoSel = años.firstOrNull() ?: return

        val meses = fechas.filter {
            Calendar.getInstance().apply { time = it }.get(Calendar.YEAR) == añoSel
        }.map {
            Calendar.getInstance().apply { time = it }.get(Calendar.MONTH) + 1
        }.distinct().sorted()
        val mesSel = meses.firstOrNull() ?: return

        actualizarRegistros(empleado, añoSel, mesSel, años, meses)
    }

    /** Selección de año */
    fun seleccionarAño(año: Int) {
        val empleado = _uiState.value.empleado ?: return
        val fechas = empleado.registrosPorDia.mapNotNull { runCatching { dateFormat.parse(it.fecha) }.getOrNull() }
        val meses = fechas.filter { Calendar.getInstance().apply { time = it }.get(Calendar.YEAR) == año }
            .map { Calendar.getInstance().apply { time = it }.get(Calendar.MONTH) + 1 }
            .distinct()
            .sorted()
        val mesSel = meses.firstOrNull() ?: return
        actualizarRegistros(empleado, año, mesSel, _uiState.value.años, meses)
    }

    /** Selección de mes */
    fun seleccionarMes(mes: Int) {
        val empleado = _uiState.value.empleado ?: return
        val año = _uiState.value.añoSeleccionado ?: return
        actualizarRegistros(empleado, año, mes, _uiState.value.años, _uiState.value.meses)
    }

    /** Filtrar registros por año y mes */
    private fun actualizarRegistros(
        empleado: EmpleadoRegistros,
        año: Int,
        mes: Int,
        años: List<Int>,
        meses: List<Int>
    ) {
        val registros = empleado.registrosPorDia.filter { dia ->
            val date = runCatching { dateFormat.parse(dia.fecha) }.getOrNull() ?: return@filter false
            val cal = Calendar.getInstance().apply { time = date }
            cal.get(Calendar.YEAR) == año && (cal.get(Calendar.MONTH) + 1) == mes
        }

        _uiState.value = _uiState.value.copy(
            años = años,
            añoSeleccionado = año,
            meses = meses,
            mesSeleccionado = mes,
            registrosFiltrados = registros,
            isLoading = false
        )
    }
}
