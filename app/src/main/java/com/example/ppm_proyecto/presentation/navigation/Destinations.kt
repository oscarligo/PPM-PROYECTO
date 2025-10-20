package com.example.ppm_proyecto.presentation.navigation.routes


/*===============================================================
Rutas de la aplicación para la navegación de la aplicación.
=================================================================*/

sealed interface AppDestination // Marca una ruta en la aplicación

data object Login : AppDestination // Pantalla de inicio de sesión

data object Register : AppDestination // Pantalla de registro de usuario

data object StudentHome : AppDestination // Pantalla principal para estudiantes

data object TeacherHome : AppDestination // Pantalla principal para profesores

data class CourseDetails(val courseId: String) : AppDestination // Detalles del curso con ID

data object Profile : AppDestination // Pantalla de perfil de usuario

data object SecuritySettings : AppDestination // Pantalla de configuración de seguridad

data object AppearanceSettings : AppDestination // Pantalla de configuración de apariencia


