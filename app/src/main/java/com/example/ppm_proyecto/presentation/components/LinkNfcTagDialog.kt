package com.example.ppm_proyecto.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LinkNfcTagDialog(
    showDialog: Boolean,
    nfcTagId: String,
    currentNfcTagId: String,
    onNfcTagIdChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean,
    errorMessage: String,
    successMessage: String
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { if (!isLoading) onDismiss() },
            title = { Text("Vincular NFC Tag") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentNfcTagId.isNotBlank()) {
                        Text(
                            text = "Tag actual: $currentNfcTagId",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedTextField(
                        value = nfcTagId,
                        onValueChange = onNfcTagIdChange,
                        label = { Text("ID del NFC Tag") },
                        placeholder = { Text("Ingrese el ID del tag") },
                        enabled = !isLoading,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (successMessage.isNotBlank()) {
                        Text(
                            text = successMessage,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (isLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    enabled = !isLoading && nfcTagId.isNotBlank()
                ) {
                    Text(if (isLoading) "Vinculando..." else "Vincular")
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

