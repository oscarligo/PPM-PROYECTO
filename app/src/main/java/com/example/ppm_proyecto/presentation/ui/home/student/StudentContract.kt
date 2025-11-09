package com.example.ppm_proyecto.presentation.ui.home.student

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.user.Notification
import com.example.ppm_proyecto.domain.models.user.User

object  StudentContract {
    sealed interface Intent {
        data class SeeCourseDetails(val courseId: String) : Intent // Navegar a los detalles del curso
        data object OpenProfile: Intent // Navegar al perfil del usuario
        data object OpenSecuritySettings: Intent // Navegar a la configuración de seguridad
        data object OpenAppearanceSettings: Intent // Navegar a la configuración de apariencia

        data object Logout: Intent // Cerrar sesión del usuario

        data class ViewNotification(val notificationId: String) : Intent // Ver una notificación específica
        data object ToggleDrawer: Intent // Abrir o cerrar el drawer lateral
        data object CloseDrawer: Intent // Cerrar el drawer lateral
    }

    data class State (
        val user: User? = null, // Usuario con sesión iniciada
        val courses: List<Course> = emptyList(), // Cursos del estudiante
        val notifications: List<Notification> = emptyList(), // Notificaciones del estudiante
        val isLoading: Boolean = false, // Indicador de carga
        val error: String = "", // Mensaje de error si ocurre alguno
        val selectedNotification: String? = null, // ID de la notificación seleccionada
        val isDrawerOpen: DrawerState = DrawerState(DrawerValue.Closed), // Estado del cajón de navegación
        // Nuevo: indica si ya se cargaron los datos iniciales (para refrescar tras logout/login)
        val loaded: Boolean = false,
        // Estadísticas de asistencia
        val presentCount: Int = 0,
        val absentCount: Int = 0,
        val lateCount: Int = 0,

        // Gráficos de las estadísticas
        val attendancePercent: Int = 0, // Porcentaje de asistencia
    )

    sealed interface Effect {

    }



}