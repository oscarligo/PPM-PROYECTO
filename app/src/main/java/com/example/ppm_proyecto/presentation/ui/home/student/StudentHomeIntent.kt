package com.example.ppm_proyecto.presentation.ui.home.student



/*=======================================================
Intents para la pantalla principal del estudiante
========================================================*/


sealed interface StudentHomeIntent {
    data class SeeCourseDetails(val courseId: String) : StudentHomeIntent // Navegar a los detalles del curso
    data object OpenProfile: StudentHomeIntent // Navegar al perfil del usuario
    data object OpenSecuritySettings: StudentHomeIntent // Navegar a la configuración de seguridad
    data object OpenAppearanceSettings: StudentHomeIntent // Navegar a la configuración de apariencia
    data class ViewNotification(val notificationId: String) : StudentHomeIntent // Ver una notificación específica

    data object ToggleDrawer: StudentHomeIntent // Abrir o cerrar el drawer lateral

    data object CloseDrawer: StudentHomeIntent // Cerrar el drawer lateral

    data class SelectCourse(val courseId: String?): StudentHomeIntent //
}