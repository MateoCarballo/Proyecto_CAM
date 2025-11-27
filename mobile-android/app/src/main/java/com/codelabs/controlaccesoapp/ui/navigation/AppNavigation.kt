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

/*
    val loginScreen = Screen(route = "loginScreen")
    val userDashboardScreen = Screen(route = "userDashboardScreen")
 */

@Composable
fun AppNavigation(tokenManager: TokenManager) {
    val loginScreen = Screen(route = "loginScreen")
    val userDashboardScreen = Screen(route = "userDashboardScreen")

    val navController = rememberNavController()

    val loginRepo = AuthRepository(tokenManager)
    val dashboardRepo = HorariosRepository(tokenManager)

    val loginViewModel: LoginScreenViewModel = viewModel(
        factory = LoginScreenViewModelFactory(
            repository = loginRepo,
            tokenManager = tokenManager
        )
    )

    val dashboardViewModel: UserDashboardViewModel = viewModel(
        factory = UserDashboardViewModelFactory(
            repository = dashboardRepo,
            tokenManager = tokenManager
        )

    )

    NavHost(
        navController = navController,
        startDestination = loginScreen.route
    ) {
        composable(loginScreen.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onRegisterClick = {},
                onLoginSuccess = {
                    navController.navigate(
                        route = userDashboardScreen.route
                    ) {
                        popUpTo(
                            route = loginScreen.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(userDashboardScreen.route) {
            val uiState by dashboardViewModel.uiState.collectAsState()
            UserDashboardScreen(
                uiState = uiState,
                clearToken = {},
                onSelectYear = { dashboardViewModel.seleccionarAnho(it) },
                onSelectMonth = { dashboardViewModel.seleccionarMes(it) },
                onLoadData = {dashboardViewModel.cargarDatos()},
                onLogout = {
                    tokenManager.saveToken("")
                    navController.navigate(
                        route = loginScreen.route
                    ) {
                        popUpTo(
                            route = navController.graph.startDestinationId
                        ) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}


// Clase para definir rutas
data class Screen(val route: String)
