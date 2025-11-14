package com.codelabs.controlaccesoapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    var email = "";
    var password = "";
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0F2027), // degradado tecnológico
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            )
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Control de acceso",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Iniciar sesión",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(40.dp))

                // Campo Email

                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    placeholder = {
                        Text(
                            "email@domain.com",
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Email,
                            contentDescription = "Email icon",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 8.dp)
                                .size(22.dp),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo Password
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    placeholder = {
                        Text("contraseña", color = Color.Gray)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Visibility,
                            contentDescription = "Password icon",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color.White
                    )

                )
                Spacer(modifier = Modifier.height(32.dp))

                // Botón Continuar
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Continuar", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Separador "o"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = DividerDefaults.Thickness,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                    Text(
                        "  o  ",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = DividerDefaults.Thickness,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Registrarse
                OutlinedButton(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(50.dp),
                    border = BorderStroke(2.dp, Color.White.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Text("Registrarse", fontSize = 16.sp)
                }
            }

            // Texto legal inferior
            Text(
                text = "Al hacer clic en continuar, aceptas nuestros Términos de Servicio y Política de Privacidad.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    LoginScreen()
}


