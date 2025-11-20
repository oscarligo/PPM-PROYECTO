package com.example.ppm_proyecto.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
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
import com.example.ppm_proyecto.domain.usecase.user.GetUserUseCase
import com.example.ppm_proyecto.domain.models.user.UserRole
import javax.inject.Inject
import com.example.ppm_proyecto.core.util.Result

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var getUserUseCase: GetUserUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PPMPROYECTOTheme {
                var userRole: String? by remember { mutableStateOf(null) }
                var authStateChanged by remember { mutableStateOf(0) }

                // Observar cambios en el estado de autenticación
                LaunchedEffect(authStateChanged) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid == null) {
                        userRole = "" // No autenticado: navegar a Login
                    } else {
                        val userResult = getUserUseCase(uid)
                        userRole = when (userResult) {
                            is Result.Ok -> {
                                val user = userResult.value
                                when (user?.role) {
                                    UserRole.Teacher -> "Teacher"
                                    UserRole.Student -> "Student"
                                    null -> "" // Si no hay usuario, ir a Login
                                }
                            }
                            is Result.Err -> "" // Si hay error, ir a Login en lugar de asumir Student
                        }
                    }
                }

                // Listener para detectar cambios de autenticación

                DisposableEffect(Unit) {
                    val authStateListener = FirebaseAuth.AuthStateListener {
                        authStateChanged++
                    }
                    FirebaseAuth.getInstance().addAuthStateListener(authStateListener)

                    onDispose {
                        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
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
