package com.example.ppm_proyecto.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ppm_proyecto.presentation.navigation.AppNavigation
import com.example.ppm_proyecto.presentation.theme.PPMPROYECTOTheme
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PPMPROYECTOTheme {
                var userRole: String? by remember { mutableStateOf(null) }

                LaunchedEffect(Unit) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid == null) {
                        userRole = "" // No autenticado: navegar a Login
                    } else {
                        userRole = try {
                            val doc = FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(uid)
                                .get()
                                .await()
                            doc.getString("role") ?: "Student"
                        } catch (t: Throwable) {
                            "Student"
                        }
                    }
                }

                if (userRole == null) {
                    // Cargando estado inicial
                    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {}
                } else {
                    AppNavigation(userRole = userRole!!)
                }
            }
        }
    }
}
