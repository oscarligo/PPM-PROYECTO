package com.example.ppm_proyecto.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.ppm_proyecto.presentation.theme.PPMPROYECTOTheme
import com.example.ppm_proyecto.presentation.theme.UniGreenDark

/**
 * TopBar para pantallas secundarias con botón de navegación hacia atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = UniGreenDark,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun SecondaryTopBarPreview() {
    PPMPROYECTOTheme {
        SecondaryTopBar(
            title = "Detalles del Curso",
            onNavigateBack = {}
        )
    }
}
