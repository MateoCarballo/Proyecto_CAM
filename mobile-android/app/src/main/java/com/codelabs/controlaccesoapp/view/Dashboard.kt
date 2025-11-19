package com.codelabs.controlaccesoapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelabs.controlaccesoapp.ui.theme.Degradado1
import com.codelabs.controlaccesoapp.ui.theme.Degradado2
import com.codelabs.controlaccesoapp.ui.theme.Degradado3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard() {

    val degradado = Brush.linearGradient(
        listOf(
            Degradado1,
            Degradado2,
            Degradado3,
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text("Control Horario")
                        Text(
                            "Gestión de asistencia del personal",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(degradado)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    degradado
                )
                .padding(padding)
        ) {
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                // Tarjeta del lado izquierdo
                CardPersona("Mateo", "admin", Modifier.weight(1f))
                CardPersona("Adrian", "usuario",Modifier.weight(1f))
            }
        }
    }



}

@Composable
fun CardPersona(
    nombreUsuario: String,
    rol: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        )
        {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Usuario",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Nombre del usuario $nombreUsuario",
                    style = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = "Rol: $rol",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    Dashboard()
}