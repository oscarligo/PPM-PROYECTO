package com.example.ppm_proyecto.presentation.ui.settings.security

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

//Pantalla de seguridad para poder hacer cambio de email o contraseña
@Composable
fun SecuritySettingsScreen(
    viewModel: SecuritySettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var showEmailEditor by remember { mutableStateOf(false) }
    var showPasswordEditor by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Mensajes de éxito / error
    val context = LocalContext.current
    LaunchedEffect(state.successMessage, state.errorMessage) {
        state.successMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        state.errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // ---------- TÍTULO ----------
        Text(
            text = "Seguridad",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(32.dp))

        //      SECCIÓN DE CORREO
        Text("Correo", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))

        // Correo actual (si no se edita)
        if (!showEmailEditor) {
            Text(
                text = state.currentEmail,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(10.dp))

            Button(onClick = { showEmailEditor = true }) {
                Text("Cambiar correo")
            }
        } else {

            // Campo: nuevo correo
            OutlinedTextField(
                value = state.newEmail,
                onValueChange = { viewModel.onIntent(SecuritySettingsIntent.ChangeNewEmail(it)) },
                label = { Text("Nuevo correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Campo: contraseña actual
            OutlinedTextField(
                value = state.oldPassword,
                onValueChange = { viewModel.onIntent(SecuritySettingsIntent.ChangeOldPassword(it)) },
                label = { Text("Contraseña actual") },
                visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onIntent(SecuritySettingsIntent.SubmitEmailChange)
                    showEmailEditor = false
                },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }

            TextButton(onClick = { showEmailEditor = false }) {
                Text("Cancelar")
            }
        }

        Spacer(Modifier.height(40.dp))


        // SECCIÓN DE CONTRASEÑA
        //
        Text("Contraseña", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))

        if (!showPasswordEditor) {
            Text(
                text = "********",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(10.dp))

            Button(onClick = { showPasswordEditor = true }) {
                Text("Cambiar contraseña")
            }

        } else {

            // Nueva contraseña
            OutlinedTextField(
                value = state.newPassword,
                onValueChange = { viewModel.onIntent(SecuritySettingsIntent.ChangeNewPassword(it)) },
                label = { Text("Nueva contraseña") },
                visualTransformation =
                if (newPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { newPasswordVisible = !newPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (newPasswordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Confirmar contraseña
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onIntent(SecuritySettingsIntent.ChangeConfirmPassword(it)) },
                label = { Text("Confirmar contraseña") },
                visualTransformation =
                if (confirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { confirmPasswordVisible = !confirmPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onIntent(SecuritySettingsIntent.SubmitPasswordChange)
                    showPasswordEditor = false
                },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar contraseña")
            }

            TextButton(onClick = { showPasswordEditor = false }) {
                Text("Cancelar")
            }
        }
    }
}
