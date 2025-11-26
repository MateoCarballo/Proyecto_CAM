package com.codelabs.controlaccesoapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelabs.controlaccesoapp.data.model.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ----------------------------
// Funciones de utilidad
// ----------------------------

fun calcularHorasDia(dia: RegistroHorarioDia): Float {
    var total = 0f
    dia.horarios.forEach {
        val entrada = it.horaEntrada.split(":").map(String::toInt)
        val salida = it.horaSalida?.split(":")?.map(String::toInt)

        if (salida != null) {
            val horas = (salida[0] + salida[1] / 60f) - (entrada[0] + entrada[1] / 60f)
            total += horas
        }
    }
    return total
}

fun colorProgreso(horas: Float): Color = when {
    horas < 8f -> Color(0xFFE53935) // rojo
    horas > 8f -> Color(0xFFFFA726) // naranja
    else -> Color(0xFF43A047) // verde
}

// ----------------------------
// Pantalla principal usuario
// ----------------------------

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(usuario: EmpleadoRegistros) {

    val registros = usuario.registrosPorDia

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fechas = registros.map { LocalDate.parse(it.fecha, formatter) }

    val años = fechas.map { it.year }.distinct().sortedDescending()

    var añoSeleccionado by remember { mutableIntStateOf(años.first()) }

    val meses = fechas.filter { it.year == añoSeleccionado }
        .map { it.monthValue }
        .distinct()
        .sorted()

    var mesSeleccionado by remember { mutableIntStateOf(meses.first()) }

    val registrosFiltrados = registros.filter {
        val fecha = LocalDate.parse(it.fecha, formatter)
        fecha.year == añoSeleccionado && fecha.monthValue == mesSeleccionado
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Mis Horarios", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(usuario.nombreEmpleado, fontSize = 14.sp)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // ---------------- AÑOS ----------------
            LazyRow(Modifier.fillMaxWidth().padding(8.dp)) {
                items(años) { año ->
                    FilterChip(
                        selected = año == añoSeleccionado,
                        onClick = { añoSeleccionado = año },
                        label = { Text(año.toString()) },
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }

            // ---------------- MESES ----------------
            LazyRow(Modifier.fillMaxWidth().padding(8.dp)) {
                items(meses) { mes ->
                    FilterChip(
                        selected = mes == mesSeleccionado,
                        onClick = { mesSeleccionado = mes },
                        label = { Text(mes.toString().padStart(2, '0')) },
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }

            // ---------------- LISTA DE DÍAS ----------------
            LazyColumn(Modifier.fillMaxSize()) {
                items(registrosFiltrados) { dia ->
                    FechaCard(dia)
                }
            }
        }
    }
}

// ----------------------------
// Tarjeta de fecha + progreso + tramos
// ----------------------------

@Composable
fun FechaCard(dia: RegistroHorarioDia) {

    val horas = calcularHorasDia(dia)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(Modifier.padding(12.dp)) {

            Text(
                "📅 ${dia.fecha}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                "Horas trabajadas: ${"%.2f".format(horas)} h",
                fontSize = 14.sp
            )

            LinearProgressIndicator(
                progress = (horas / 8f).coerceIn(0f, 1f),
                color = colorProgreso(horas),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .padding(vertical = 6.dp)
            )

            // Tramos horarios
            dia.horarios.forEach { tramo ->
                TramoHorarioItem(tramo)
            }
        }
    }
}

// ----------------------------
// Fila de tramo horario
// ----------------------------

@Composable
fun TramoHorarioItem(tramo: TramoHorario) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF1565C0))
        Spacer(Modifier.width(8.dp))
        Text("${tramo.horaEntrada} - ${tramo.horaSalida ?: "--"}")
    }
}

fun dummyUsuarioDesdeJson(): EmpleadoRegistros {
    return EmpleadoRegistros(
        idEmpleado = 2,
        nombreEmpleado = "Fernando Alonso",
        numeroTarjeta = "B3AB430E",
        registrosPorDia = listOf(
            // ---------------------------
            // 1) MENOS DE 8 HORAS (6h 30m)
            // ---------------------------
            RegistroHorarioDia(
                fecha = "2025-11-25",
                horarios = listOf(
                    TramoHorario("08:00", "12:30"), // 4h 30m
                    TramoHorario("15:00", "17:00")  // 2h
                )
            ),

            // -----------------------------------
            // 2) EXACTAMENTE 8 HORAS (8h exactas)
            // -----------------------------------
            RegistroHorarioDia(
                fecha = "2025-11-26",
                horarios = listOf(
                    TramoHorario("08:00", "12:00"), // 4h
                    TramoHorario("13:00", "17:00")  // 4h
                )
            ),

            // ---------------------------
            // 3) MÁS DE 8 HORAS (9h 15m)
            // ---------------------------
            RegistroHorarioDia(
                fecha = "2025-11-27",
                horarios = listOf(
                    TramoHorario("08:00", "13:30"), // 5h 30m
                    TramoHorario("14:00", "17:45")  // 3h 45m
                )
            ),

            // ---------------------------
            // 4) Otro día estándar (<8h)
            // ---------------------------
            RegistroHorarioDia(
                fecha = "2025-11-28",
                horarios = listOf(
                    TramoHorario("08:15", "12:15"), // 4h
                    TramoHorario("13:30", "16:45")  // 3h 15m → Total 7h 15m
                )
            ),

            // ---------------------------
            // 5) Otro día con >8h (8h 30m)
            // ---------------------------
            RegistroHorarioDia(
                fecha = "2025-11-29",
                horarios = listOf(
                    TramoHorario("07:50", "12:10"), // 4h 20m
                    TramoHorario("13:00", "17:10")  // 4h 10m → Total 8h 30m
                )
            )
        )
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserDashboardPreview() {
    val usuario = dummyUsuarioDesdeJson()
    UserDashboardScreen(usuario = usuario)
}



