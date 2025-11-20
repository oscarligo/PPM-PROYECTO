package com.example.ppm_proyecto.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun JoinCourseDialog(
    showDialog: Boolean,
    courseId: String,
    onCourseIdChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String = ""
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    onDismiss()
                }
            },
            title = {
                Text(text = "Unirse a un curso")
            },
            text = {
                Column {
                    Text(
                        text = "Ingresa el ID del curso al que deseas unirte",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface

                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = courseId,
                        onValueChange = onCourseIdChange,
                        label = { Text("ID del curso") },
                        placeholder = { Text("Ej: ABC123") },
                        singleLine = true,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorMessage.isNotEmpty()
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
                    enabled = !isLoading && courseId.isNotBlank()
                ) {
                    Text("Unirse")
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
fun JoinCourseDialogPreview() {

    JoinCourseDialog(
        showDialog = true,
        courseId = "",
        onCourseIdChange = { },
        onDismiss = { },
        onConfirm = { /* Acción de unirse al curso */ },
        isLoading = false,
        errorMessage = ""
    )

}
