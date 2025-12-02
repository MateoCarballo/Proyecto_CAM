package com.codelabs.controlaccesoapp.ui.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codelabs.controlaccesoapp.data.repository.AuthRepository
import com.codelabs.controlaccesoapp.data.repository.HorariosRepository
import com.codelabs.controlaccesoapp.data.repository.TokenManager
import com.codelabs.controlaccesoapp.ui.screens.UserDashboardScreen
import com.codelabs.controlaccesoapp.ui.viewmodel.LoginScreenViewModel
import com.codelabs.controlaccesoapp.ui.viewmodel.LoginScreenViewModelFactory
import com.codelabs.controlaccesoapp.ui.viewmodel.UserDashboardViewModel
import com.codelabs.controlaccesoapp.ui.viewmodel.UserDashboardViewModelFactory

@Composable
fun AppNavigation(tokenManager: TokenManager) {
    val navController = rememberNavController()

// Repositorios fuera del NavHost para no recrearlos
    val loginRepository = AuthRepository(tokenManager)
    val dashboardRepository = HorariosRepository(tokenManager)

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val loginViewModel: LoginScreenViewModel = viewModel(
                factory = LoginScreenViewModelFactory(
                    repository = loginRepository,
                    tokenManager = tokenManager
                )
            )

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            val dashboardViewModel: UserDashboardViewModel = viewModel(
                factory = UserDashboardViewModelFactory(
                    repository = dashboardRepository,
                    tokenManager = tokenManager
                )
            )

            val uiState by dashboardViewModel.uiState.collectAsState()

            UserDashboardScreen(
                uiState = uiState,
                onLoadData = { dashboardViewModel.cargarDatos() },
                onSelectYear = { dashboardViewModel.seleccionarAnho(it) },
                onSelectMonth = { dashboardViewModel.seleccionarMes(it) },
                onLogout = {
                    dashboardViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
