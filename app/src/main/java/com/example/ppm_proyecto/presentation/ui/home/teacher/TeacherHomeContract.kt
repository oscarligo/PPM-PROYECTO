package com.example.ppm_proyecto.presentation.ui.home.teacher

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination

/**
 * Este archivo define el contrato de TeacherHome.
 */
class TeacherContract {

    /**
     * Representa todo el estado de la pantalla principal del maestro
     * La idea es que Compose solo lea este estado y nunca lo modifique directamente
     */
    data class State(
        val isLoading: Boolean = false,
        val loaded: Boolean = false,
        val user: User? = null,
        val courses: List<Course> = emptyList(),
        val error: String = "",
        val isDrawerOpen: DrawerState = DrawerState(DrawerValue.Closed), //Estado de menu lateral
    )

    /**
     * Define todas las acciones (intents) que el usuario puede realizar en esta pantalla
     */
    sealed class Intent {
        // Navegar a los detalles de un curso específico
        data class OpenCourseDetails(val courseId: String) : Intent()

        // Abrir perfil 
        data object OpenProfile : Intent()

        // Abrir pantalla de seguridad 
        data object OpenSecuritySettings : Intent()

        // Abrir ajustes de apariencia 
        data object OpenAppearanceSettings : Intent()

        // Cerrar sesión 
        data object Logout : Intent()

        // Abrir el menú 
        data object ToggleDrawer : Intent()

        // Cerrar el menú 
        data object CloseDrawer : Intent()
    }
}
