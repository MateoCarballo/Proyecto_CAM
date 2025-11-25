import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelabs.controlaccesoapp.data.api.AuthRepository
import com.codelabs.controlaccesoapp.ui.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            Text("Control de acceso", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                placeholder = { Text("email@domain.com", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Rounded.Email, null, tint = Color.White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.85f),
                textStyle = TextStyle(Color.White)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = { Text("contraseña", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Rounded.Visibility, null, tint = Color.White) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(0.85f),
                textStyle = TextStyle(Color.White)
            )
            Spacer(Modifier.height(16.dp))

            if (uiState.errorMessage != null) {
                Text(uiState.errorMessage, color = Color.Red, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.login() },
                modifier = Modifier.fillMaxWidth(0.85f),
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Cargando..." else "Continuar")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth(0.85f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
            ) {
                Text("Registrarse", color = Color.White)
            }
        }
    }

    // Navegación en caso de login exitoso
    if (uiState.isLoginSuccess) {
        onLoginSuccess()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val dummyViewModel = LoginViewModel(AuthRepository())
    LoginScreen(dummyViewModel)
}
