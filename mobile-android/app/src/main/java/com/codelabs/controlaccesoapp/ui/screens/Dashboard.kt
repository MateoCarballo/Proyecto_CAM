package com.codelabs.controlaccesoapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelabs.controlaccesoapp.ui.theme.Degradado1
import com.codelabs.controlaccesoapp.ui.theme.Degradado2
import com.codelabs.controlaccesoapp.ui.theme.Degradado3

// ----------------------------
// Datos de prueba para preview
// ----------------------------
data class Registro(
    val fecha: String,
    val horaEntrada: String,
    val horaSalida: String?,
    val tipo: String // "entrada" o "salida"
)

data class Empleado(
    val id: Int,
    val nombre: String,
    val rol: String,
    val horasTrabajadas: Float, // Total de horas para la jornada
    val registros: List<Registro>
)

val empleadosDummy = listOf(
    Empleado(
        1, "Fernando Alonso", "admin", 7.5f, listOf(
            Registro("2025-11-25", "08:00", "13:30", "entrada"),
            Registro("2025-11-25", "15:00", "18:00", "entrada")
        )
    ),
    Empleado(
        2, "Lewis Hamilton", "usuario", 8f, listOf(
            Registro("2025-11-25", "08:15", "12:30", "entrada"),
            Registro("2025-11-25", "13:15", "17:00", "entrada")
        )
    ),
    Empleado(
        3, "Sebastian Vettel", "usuario", 9.2f, listOf(
            Registro("2025-11-25", "09:00", "12:45", "entrada"),
            Registro("2025-11-25", "13:30", "18:15", "entrada")
        )
    )
)

// ----------------------------
// Función para color dinámico según horas trabajadas
// ----------------------------
@Composable
fun progresoColor(horas: Float): Color {
    return when {
        horas < 8f -> Color.Red
        horas > 8f -> Color(0xFFFFA500) // Amarillo/Naranja
        else -> Color(0xFF4CAF50) // Verde
    }
}

// ----------------------------
// Dashboard Principal
// ----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    empleados: List<Empleado> = emptyList(),
    isLoading: Boolean = false
) {
    val degradado = Brush.linearGradient(listOf(Degradado1, Degradado2, Degradado3))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Control Horario",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                        Text(
                            "Gestión de asistencia del personal",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black.copy(alpha = 0.85f)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(degradado)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(degradado)
                .padding(padding)
        ) {
            if (isLoading) {
                // Placeholder Shimmer
                Column(modifier = Modifier.padding(16.dp)) {
                    repeat(3) {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.3f))
                            )
                        }
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(empleados) { empleado ->
                        CardEmpleado(empleado)
                    }
                }
            }
        }
    }
}

// ----------------------------
// Tarjeta de empleado con registros y barra de progreso
// ----------------------------
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardEmpleado(empleado: Empleado) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = empleado.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Rol: ${empleado.rol}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "Expandir",
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de progreso de horas trabajadas
            Text(
                text = "Horas trabajadas: ${empleado.horasTrabajadas} / 8",
                style = MaterialTheme.typography.bodyMedium
            )

            LinearProgressIndicator(
                progress = (empleado.horasTrabajadas / 8f).coerceAtMost(1f),
                color = progresoColor(empleado.horasTrabajadas),
                trackColor = Color.Gray.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(vertical = 4.dp)
            )


            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    empleado.registros.forEach { registro ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = "Fecha",
                                tint = if (registro.tipo == "entrada") Color(0xFF4CAF50) else Color(0xFFF44336),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "${registro.fecha} | ${registro.horaEntrada} - ${registro.horaSalida ?: "-"}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------
// Preview con datos dummy
// ----------------------------
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    Dashboard(empleadosDummy, isLoading = false)
}
