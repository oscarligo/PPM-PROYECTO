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

        data object RefreshUserData: Intent // Nuevo: refrescar datos del usuario

        data object OpenJoinCourseDialog: Intent // Abrir diálogo para unirse a un curso
        data object CloseJoinCourseDialog: Intent // Cerrar diálogo para unirse a un curso
        data class UpdateJoinCourseId(val courseId: String): Intent // Actualizar el ID del curso a unirse
        data object JoinCourse: Intent // Unirse a un curso

        // Nuevos intents para NFC
        data class MarkAttendanceViaTag(val nfcTagId: String): Intent // Marcar asistencia con tag NFC
    }

    data class State (
        val user: User? = null, // Usuario con sesión iniciada
        val courses: List<Course> = emptyList(), // Cursos del estudiante
        val notifications: List<Notification> = emptyList(), // Notificaciones del estudiante
        val isLoading: Boolean = false, // Indicador de carga
        val error: String = "", // Mensaje de error si ocurre alguno
        val selectedNotification: String? = null, // ID de la notificación seleccionada
        val isDrawerOpen: DrawerState = DrawerState(DrawerValue.Closed), // Estado del cajón de navegación
        val loaded: Boolean = false, // Indica si los datos fueron cargados

        // Estadísticas de asistencia
        val presentCount: Int = 0,
        val absentCount: Int = 0,
        val lateCount: Int = 0,

        // Gráficos de las estadísticas
        val attendancePercent: Int = 0, // Porcentaje de asistencia

        // Estado del diálogo de unirse a curso
        val showJoinCourseDialog: Boolean = false,
        val joinCourseId: String = "",
        val joinCourseLoading: Boolean = false,
        val joinCourseError: String = "",

        // Estado de marcado de asistencia por NFC
        val nfcAttendanceMessage: String = "",
        val nfcAttendanceSuccess: Boolean = false,
    )

    sealed interface Effect {

    }



}