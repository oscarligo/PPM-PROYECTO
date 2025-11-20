package com.example.ppm_proyecto.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppAB(
    onClick: () -> Unit,
    text: String = "Unirse a curso",
    expanded: Boolean = true
) {
    if (expanded) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = { Icon(Icons.Filled.Add, contentDescription = text) },
            text = { Text(text = text) }
        )
    } else {
        androidx.compose.material3.FloatingActionButton(
            onClick = onClick,
        ) {
            Icon(Icons.Filled.Add, contentDescription = text)
        }
    }
}

@Preview
@Composable
fun AppABPreview() {
    AppAB(onClick = {}, expanded = true)
}

