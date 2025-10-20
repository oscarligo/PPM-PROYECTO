// Ubicación: presentation/ui/register/RegisterScreen.kt
package com.example.ppm_proyecto.presentation.ui.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//PPMPROYECTOTheme {




@Preview
@Composable

fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {

    var selectedRole by remember { mutableStateOf("Estudiante") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título principal
        Text(
            text = "Crea una cuenta",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Subtítulo antes del selector
        Text(
            text = "¿Para quién es la cuenta?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selector animado Maestro / Estudiante
        RoleSelector(
            selectedRole = selectedRole,
            onRoleSelected = { selectedRole = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Label "Usuario"
        Text(
            text = "Usuario",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto: Usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
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

        Spacer(modifier = Modifier.height(20.dp))

        // Label "Correo electrónico"
        Text(
            text = "Correo electrónico",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto: Correo electrónico
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("ejemplo@correo.com") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

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
            value = password,
            onValueChange = { password = it },
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

        Spacer(modifier = Modifier.height(40.dp))

        // Botón principal: Crear cuenta
        Button(
            onClick = onRegisterSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun RoleSelector(
    selectedRole: String,
    onRoleSelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Botón Maestro
            RoleButton(
                text = "Maestro",
                isSelected = selectedRole == "Maestro",
                onClick = { onRoleSelected("Maestro") },
                modifier = Modifier.weight(1f)
            )

            // Botón Estudiante
            RoleButton(
                text = "Estudiante",
                isSelected = selectedRole == "Estudiante",
                onClick = { onRoleSelected("Estudiante") },
                modifier = Modifier.weight(1f)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Animación que cambia según el rol seleccionado
    AnimatedContent(
        targetState = selectedRole,
        transitionSpec = {
            (slideInHorizontally(
                initialOffsetX = { if (targetState == "Maestro") -it else it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))).togetherWith(
                slideOutHorizontally(
                    targetOffsetX = { if (targetState == "Maestro") it else -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            )
        },
        label = "role_animation"
    ) { role ->
        Text(
            text = if (role == "Maestro") "📚 Cuenta para profesores" else "🎓 Cuenta para estudiantes",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RoleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}