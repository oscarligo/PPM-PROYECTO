package com.example.ppm_proyecto.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Cuadro de dialogo para crear un nuevo curso.
 */
@Composable
fun CreateCourseDialog(
    showDialog: Boolean, // Indica si mostrar el diálogo
    courseName: String, // nombre del curso
    courseDescription: String, // Descripcion del curso
    onCourseNameChange: (String) -> Unit, // Callback para cambios en el nombre del curso
    onCourseDescriptionChange: (String) -> Unit, // Callback para cambios en la descripción del curso
    onDismiss: () -> Unit, // Cerrar el diálogo
    onConfirm: () -> Unit, // Confirmar la creación del curso
    isLoading: Boolean = false, // Indica si se está procesando la creación
    errorMessage: String = "" // Mensaje de error a mostrar
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    onDismiss()
                }
            },
            title = {
                Text(text = "Crear nuevo curso")
            },
            text = {
                Column {
                    Text(
                        text = "Completa la información del curso",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo para el nombre del curso
                    OutlinedTextField(
                        value = courseName,
                        onValueChange = onCourseNameChange,
                        label = { Text("Nombre del curso") },
                        placeholder = { Text("Ej: Programación Móvil") },
                        singleLine = true,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorMessage.isNotEmpty()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo para la descripción del curso
                    OutlinedTextField(
                        value = courseDescription,
                        onValueChange = onCourseDescriptionChange,
                        label = { Text("Descripción (opcional)") },
                        placeholder = { Text("Breve descripción del curso") },
                        maxLines = 3,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    enabled = !isLoading && courseName.isNotBlank()
                ) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    enabled = !isLoading
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview
@Composable
fun CreateCourseDialogPreview() {
    CreateCourseDialog(
        showDialog = true,
        courseName = "",
        courseDescription = "",
        onCourseNameChange = { },
        onCourseDescriptionChange = { },
        onDismiss = { },
        onConfirm = { },
        isLoading = false,
        errorMessage = ""
    )
}

