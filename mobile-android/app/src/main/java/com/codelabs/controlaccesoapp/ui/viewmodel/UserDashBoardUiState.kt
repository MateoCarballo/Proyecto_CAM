package com.codelabs.controlaccesoapp.ui.viewmodel

import com.codelabs.controlaccesoapp.data.model.EmpleadoRegistros
import com.codelabs.controlaccesoapp.data.model.RegistroHorarioDia

data class UserDashBoardUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val años: List<Int> = emptyList(),
    val añoSeleccionado: Int? = null,

    val meses: List<Int> = emptyList(),
    val mesSeleccionado: Int? = null,

    val registrosFiltrados: List<RegistroHorarioDia> = emptyList(),

    // Los datos del empleado (vienen del login)
    val empleado: EmpleadoRegistros? = null
)
