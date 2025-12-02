import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelabs.controlaccesoapp.ui.viewmodel.LoginScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel,
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Mostrar errores como Snackbar y limpiar el error después
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                    )
                )
                .padding(32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Control de acceso",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChanged,
                    placeholder = { Text("email@domain.com", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Rounded.Email, null, tint = Color.White) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.85f),
                    textStyle = TextStyle(Color.White)
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChanged,
                    placeholder = { Text("contraseña", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Rounded.Visibility, null, tint = Color.White) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.85f),
                    textStyle = TextStyle(Color.White)
                )
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    enabled = !uiState.isLoading
                ) {
                    Text(if (uiState.isLoading) "Cargando..." else "Continuar")
                }

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.resetState()
                        onRegisterClick()
                    },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
                ) {
                    Text("Registrarse", color = Color.White)
                }
            }
        }
    }

    if (uiState.isLoginSuccess) {
        onLoginSuccess()
    }
}
