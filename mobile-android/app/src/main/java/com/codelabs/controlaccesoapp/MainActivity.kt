package com.codelabs.controlaccesoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.codelabs.controlaccesoapp.data.repository.TokenManager
import com.codelabs.controlaccesoapp.ui.navigation.AppNavigation
import com.codelabs.controlaccesoapp.ui.theme.ControlAccesoAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tokenManager = TokenManager.getInstance(this)

        setContent {
            ControlAccesoAppTheme {
                AppNavigation(
                    tokenManager
                )
            }
        }
    }
}