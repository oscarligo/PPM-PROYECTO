package com.example.ppm_proyecto.presentation.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.theme.PPMPROYECTOTheme
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    viewModel : LoginViewModel = hiltViewModel(),
    navigate: (AppDestination) -> Unit
) {

    val state by viewModel.state

    // Contenedor principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Spacer y una imagen
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = "https://altiplano.uvg.edu.gt/nosotros/img/png/Logo%20UVG-%20Colores.png", // URL de la imagen
            contentDescription = "Descripción de la imagen", // Para accesibilidad
            modifier = Modifier
                .size(100.dp) // Tamaño de la imagen

        )
        Spacer(modifier = Modifier.height(16.dp))

        // Nombre de la aplicación
        Text(
            text = "Registro de Asistencia",
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 40.dp),
            color = MaterialTheme.colorScheme.primary
        )


        // Label "Usuario"
        Text(
            text = "Usuario",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de usuario
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onIntent(LoginContract.Intent.SetEmail(it), navigate) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingresa tu usuario") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Usuario"
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Label "Contraseña"
        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto: Contraseña
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onIntent(LoginContract.Intent.SetPassword(it), navigate) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Mínimo 8 caracteres") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Contraseña"
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // Mensaje de error
        if (state.error.isNotEmpty()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Botón para iniciar sesión
        Button(
            onClick = { viewModel.onIntent(LoginContract.Intent.Submit, navigate) },
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Iniciar sesión"
            )
        }

        // Botón secundario
        TextButton(
            onClick = { viewModel.onIntent(LoginContract.Intent.GoToRegister, navigate) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("¿No tienes una cuenta? Crea una.")
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    PPMPROYECTOTheme {
        // Preview sin Hilt
        // LoginScreen(navigate = {})
    }
}