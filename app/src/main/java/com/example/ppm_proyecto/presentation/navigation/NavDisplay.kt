package com.example.ppm_proyecto.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.AppearanceSettings
import com.example.ppm_proyecto.presentation.navigation.routes.CourseDetails
import com.example.ppm_proyecto.presentation.navigation.routes.Login
import com.example.ppm_proyecto.presentation.navigation.routes.Profile
import com.example.ppm_proyecto.presentation.navigation.routes.Register
import com.example.ppm_proyecto.presentation.navigation.routes.SecuritySettings
import com.example.ppm_proyecto.presentation.navigation.routes.StudentHome
import com.example.ppm_proyecto.presentation.navigation.routes.TeacherHome
import com.example.ppm_proyecto.presentation.ui.home.student.StudentHomeScreen
import com.example.ppm_proyecto.presentation.ui.login.LoginScreen
import com.example.ppm_proyecto.presentation.ui.register.RegisterScreen


/*=======================================================================
Función principal que maneja la navegación de la aplicación. Actualmente
sin un inicio de sesión real, solo con fines de demostración de la
interfaz de usuario.
=======================================================================*/

@Composable
fun AppNavigation(userRole: String) {

    // Determinar pantalla inicial según rol
    val startScreen: AppDestination = when (userRole) {
        "Student" -> StudentHome
        "Teacher" -> TeacherHome
        else -> Login
    }

    val backStack = remember { mutableStateListOf<AppDestination>(startScreen) } // Backstack

    fun navigate(to: AppDestination) { backStack.add(to) }
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun back() { if (backStack.size > 1) backStack.removeLast() }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {

        NavDisplay(
            backStack = backStack,
            onBack = { back() },
            entryProvider = { key ->
                when (key) {
                    is Login -> NavEntry(key) { LoginScreen { dest -> navigate(dest) } }
                    is Register -> NavEntry(key) { RegisterScreen { dest -> navigate(dest) } }
                    is StudentHome -> NavEntry(key) { StudentHomeScreen { dest -> navigate(dest) } }
                    is TeacherHome -> NavEntry(key) { /* TeacherHomeScreen.kt */ }
                    is CourseDetails -> NavEntry(key) { /* CourseDetailsScreen */ }
                    is Profile -> NavEntry(key) { /* ProfileScreen */ }
                    is SecuritySettings -> NavEntry(key) { /* SecuritySettingsScreen */ }
                    is AppearanceSettings -> NavEntry(key) { /* AppearanceSettingsScreen */ }
                }
            }
        )
    }
}
