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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.ppm_proyecto.presentation.ui.settings.security.SecurityContract.SecuritySettingsState
import com.example.ppm_proyecto.presentation.ui.settings.security.SecurityContract.SecuritySettingsIntent

//Pantalla de seguridad para poder hacer cambio de email o contraseña
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecuritySettingsScreen(
    viewModel: SecuritySettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguridad") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(Modifier.height(24.dp))

            // ========== SECCIÓN DE CORREO ==========
            Text(
                text = "Correo electrónico",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))

            // Correo actual (si no se edita)
            if (!showEmailEditor) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Correo actual",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = state.currentEmail,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { showEmailEditor = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cambiar correo")
                }
            } else {
                // Formulario de cambio de correo
                OutlinedTextField(
                    value = state.newEmail,
                    onValueChange = { viewModel.onIntent(SecuritySettingsIntent.ChangeNewEmail(it)) },
                    label = { Text("Nuevo correo") },
                    placeholder = { Text("ejemplo@correo.com") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                )

                Spacer(Modifier.height(12.dp))

                // Campo: contraseña actual para confirmar
                OutlinedTextField(
                    value = state.oldPassword,
                    onValueChange = { viewModel.onIntent(SecuritySettingsIntent.ChangeOldPassword(it)) },
                    label = { Text("Contraseña actual") },
                    placeholder = { Text("Confirma tu contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
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
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Guardar cambios")
                    }
                }

                OutlinedButton(
                    onClick = {
                        showEmailEditor = false
                        viewModel.onIntent(SecuritySettingsIntent.ChangeNewEmail(""))
                        viewModel.onIntent(SecuritySettingsIntent.ChangeOldPassword(""))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }

            Spacer(Modifier.height(40.dp))

            // ========== SECCIÓN DE CONTRASEÑA ==========
            Text(
                text = "Contraseña",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))

            if (!showPasswordEditor) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Contraseña",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "••••••••",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { showPasswordEditor = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cambiar contraseña")
                }

            } else {
                // Formulario de cambio de contraseña
                OutlinedTextField(
                    value = state.newPassword,
                    onValueChange = { viewModel.onIntent(SecuritySettingsIntent.ChangeNewPassword(it)) },
                    label = { Text("Nueva contraseña") },
                    placeholder = { Text("Mínimo 6 caracteres") },
                    visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = if (newPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                )

                Spacer(Modifier.height(12.dp))

                // Confirmar contraseña
                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = { viewModel.onIntent(SecuritySettingsIntent.ChangeConfirmPassword(it)) },
                    label = { Text("Confirmar nueva contraseña") },
                    placeholder = { Text("Repite la contraseña") },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
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
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Guardar contraseña")
                    }
                }

                OutlinedButton(
                    onClick = {
                        showPasswordEditor = false
                        viewModel.onIntent(SecuritySettingsIntent.ChangeNewPassword(""))
                        viewModel.onIntent(SecuritySettingsIntent.ChangeConfirmPassword(""))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
