package com.example.ppm_proyecto.presentation.ui.home.teacher

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.user.User

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

        // Estadísticas de asistencia
        val presentCount: Int = 0,
        val absentCount: Int = 0,
        val lateCount: Int = 0,
        val attendancePercent: Int = 0, // Porcentaje de asistencia

        // Estado del diálogo de crear curso
        val showCreateCourseDialog: Boolean = false,
        val createCourseName: String = "",
        val createCourseDescription: String = "",
        val createCourseLoading: Boolean = false,
        val createCourseError: String = "",

        // Estado del diálogo de vincular NFC tag
        val showLinkNfcTagDialog: Boolean = false,
        val nfcTagIdInput: String = "",
        val linkNfcTagLoading: Boolean = false,
        val linkNfcTagError: String = "",
        val linkNfcTagSuccess: String = ""
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

        // Abrir diálogo de crear curso
        data object OpenCreateCourseDialog : Intent()

        // Cerrar diálogo de crear curso
        data object CloseCreateCourseDialog : Intent()

        // Actualizar campos del formulario de crear curso
        data class UpdateCourseName(val name: String) : Intent()
        data class UpdateCourseDescription(val description: String) : Intent()

        // Crear curso
        data object CreateCourse : Intent()

        // Refrescar datos
        data object RefreshData : Intent()

        // Abrir diálogo de vincular NFC tag
        data object OpenLinkNfcTagDialog : Intent()

        // Cerrar diálogo de vincular NFC tag
        data object CloseLinkNfcTagDialog : Intent()

        // Actualizar campo de ID de NFC tag
        data class UpdateNfcTagId(val id: String) : Intent()

        // Vincular NFC tag
        data object LinkNfcTag : Intent()
    }
}
