package com.codelabs.controlaccesoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codelabs.controlaccesoapp.data.model.DailyRecord
import com.codelabs.controlaccesoapp.ui.viewmodel.UserDashBoardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(
    uiState: UserDashBoardUiState,
    onLoadData: () -> Unit,
    onSelectYear: (Int) -> Unit,
    onSelectMonth: (Int) -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { onLoadData() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
                return@Column
            }

            uiState.errorMessage?.let {
                Text("Error: $it", color = MaterialTheme.colorScheme.error)
                return@Column
            }

            uiState.dashboardUser?.employeeInfo?.let { emp ->
                Text(
                    text = "Welcome, ${emp.nombreEmpleado}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (uiState.years.isNotEmpty()) {
                Text("Available Years")
                Spacer(Modifier.height(8.dp))
                uiState.years.forEach { year ->
                    Button(
                        onClick = { onSelectYear(year) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) { Text(year.toString()) }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.months.isNotEmpty()) {
                Text("Available Months")
                Spacer(Modifier.height(8.dp))
                uiState.months.forEach { month ->
                    Button(
                        onClick = { onSelectMonth(month) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) { Text("Month ${month.toString().padStart(2, '0')}") }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.filteredRecords) { day ->
                    DailyRecordItem(day)
                }
            }
        }
    }

}

@Composable
fun DailyRecordItem(day: DailyRecord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "📅 ${day.fecha}", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(6.dp))
            day.horarios.forEach { range ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "${range.horaEntrada} - ${range.horaSalida ?: "--"}")
                }
            }
        }
    }
}
