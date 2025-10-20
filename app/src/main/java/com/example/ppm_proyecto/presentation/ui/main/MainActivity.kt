package com.example.ppm_proyecto.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ppm_proyecto.presentation.theme.PPMPROYECTOTheme
import com.example.ppm_proyecto.presentation.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PPMPROYECTOTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(userRole = "Student")
                }
            }
        }
    }
}
