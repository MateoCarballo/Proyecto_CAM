package com.codelabs.controlaccesoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codelabs.controlaccesoapp.data.repository.HorariosRepository
import com.codelabs.controlaccesoapp.data.repository.TokenManager

/** Factory para inyectar repositorio y token */
class UserDashboardViewModelFactory(
    private val repository: HorariosRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserDashboardViewModel(repository, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}