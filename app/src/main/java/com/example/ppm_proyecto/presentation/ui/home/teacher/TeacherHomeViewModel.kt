package com.example.ppm_proyecto.presentation.ui.home.teacher

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.usecase.auth.CurrentUserUseCase
import com.example.ppm_proyecto.domain.usecase.auth.LogoutUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetTeacherCoursesUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetCourseSessionsForTeacherUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetSessionAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.CreateCourseUseCase
import com.example.ppm_proyecto.domain.usecase.user.GetUserUseCase
import com.example.ppm_proyecto.domain.usecase.user.UpdateUserNfcTagUseCase
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.CourseDetailsTeacher
import com.example.ppm_proyecto.presentation.navigation.routes.Login
import com.example.ppm_proyecto.presentation.navigation.routes.Profile
import com.example.ppm_proyecto.presentation.navigation.routes.SecuritySettings
import com.example.ppm_proyecto.presentation.navigation.routes.AppearanceSettings
import kotlin.math.roundToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que controla la lógica de la pantalla principal del profesor.
 */
@HiltViewModel
class TeacherHomeViewModel @Inject constructor(
    private val getTeacherCoursesUseCase: GetTeacherCoursesUseCase,
    private val getCourseSessionsForTeacherUseCase: GetCourseSessionsForTeacherUseCase,
    private val getSessionAttendanceUseCase: GetSessionAttendanceUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserNfcTagUseCase: UpdateUserNfcTagUseCase
) : ViewModel() {

    // Estado de la pantalla
    var state = mutableStateOf(TeacherContract.State())
        private set // Para que solo el ViewModel pueda modificarlo

    //Este método garantiza que la información del maestro solo se cargue una vez
    fun ensureLoaded() {
        if (!state.value.loaded && !state.value.isLoading) {
            loadTeacherData()
        }
    }

    // Maneja las diferentes acciones (Intents) que el usuario puede realizar desde la UI
    fun onIntent(intent: TeacherContract.Intent, navigate: (AppDestination) -> Unit) {
        when (intent) {
            is TeacherContract.Intent.OpenCourseDetails -> {
                navigate(CourseDetailsTeacher(intent.courseId))
            }
            TeacherContract.Intent.OpenProfile -> navigate(Profile)
            TeacherContract.Intent.OpenSecuritySettings -> navigate(SecuritySettings)
            TeacherContract.Intent.OpenAppearanceSettings -> navigate(AppearanceSettings)
            TeacherContract.Intent.Logout -> logout(navigate)
            TeacherContract.Intent.ToggleDrawer -> openDrawer()
            TeacherContract.Intent.CloseDrawer -> closeDrawer()
            TeacherContract.Intent.OpenCreateCourseDialog -> openCreateCourseDialog()
            TeacherContract.Intent.CloseCreateCourseDialog -> closeCreateCourseDialog()
            is TeacherContract.Intent.UpdateCourseName -> updateCourseName(intent.name)
            is TeacherContract.Intent.UpdateCourseDescription -> updateCourseDescription(intent.description)
            TeacherContract.Intent.CreateCourse -> createCourse()
            TeacherContract.Intent.RefreshData -> loadTeacherData()
            TeacherContract.Intent.OpenLinkNfcTagDialog -> openLinkNfcTagDialog()
            TeacherContract.Intent.CloseLinkNfcTagDialog -> closeLinkNfcTagDialog()
            is TeacherContract.Intent.UpdateNfcTagId -> updateNfcTagId(intent.id)
            TeacherContract.Intent.LinkNfcTag -> linkNfcTag()
        }
    }

    /**
     * Carga la información principal del maestro:
     * - Perfil del usuario (nombre, email)
     * - Lista de cursos asignados
     * - Estadísticas de asistencia de todos los cursos
     */
    private fun loadTeacherData() {
        viewModelScope.launch {
            // Actualiza el estado a "cargando" y limpia errores previos
            state.value = state.value.copy(isLoading = true, error = "")

            // Obtiene el ID del usuario 
            val teacherId = currentUserUseCase()
            if (teacherId == null) {
                // Si no hay sesión se muestra error y detiene la carga
                state.value = state.value.copy(isLoading = false, error = "Sesión no encontrada")
                return@launch
            }

            //  Cargar perfil del maestro
            when (val resProfile = getUserUseCase(teacherId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(user = resProfile.value)
                }
                is Result.Err -> {
                    state.value = state.value.copy(error = "Error cargando perfil: ${resProfile.throwable.message}")
                }
            }

            // Cargar cursos del maestro
            when (val resCourses = getTeacherCoursesUseCase(teacherId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(courses = resCourses.value)

                    // Calcular estadísticas de asistencia de todos los cursos
                    calculateAttendanceStats(resCourses.value)
                }
                is Result.Err -> {
                    state.value = state.value.copy(error = "Error cargando cursos: ${resCourses.throwable.message}")
                }
            }

            // Finaliza la carga y marca el estado como "cargado correctamente"
            state.value = state.value.copy(isLoading = false, loaded = true)
        }
    }

    /**
     * Calcula las estadísticas de asistencia de todos los cursos del profesor
     */
    private suspend fun calculateAttendanceStats(courses: List<com.example.ppm_proyecto.domain.models.course.Course>) {
        try {
            val allAttendances = mutableListOf<SessionAttendance>()

            // Para cada curso, obtener todas las sesiones y sus asistencias
            courses.forEach { course ->
                when (val sessionsResult = getCourseSessionsForTeacherUseCase(course.id)) {
                    is Result.Ok -> {
                        val sessions = sessionsResult.value
                        // Para cada sesión, obtener las asistencias
                        sessions.forEach { session ->
                            when (val attendanceResult = getSessionAttendanceUseCase(course.id, session.id)) {
                                is Result.Ok -> {
                                    allAttendances.addAll(attendanceResult.value)
                                }
                                is Result.Err -> { /* Ignorar errores individuales */ }
                            }
                        }
                    }
                    is Result.Err -> { /* Ignorar errores individuales */ }
                }
            }

            // Calcular estadísticas
            val present = allAttendances.count { it.status == AttendanceStatus.Present }
            val absent = allAttendances.count { it.status == AttendanceStatus.Absent }
            val late = allAttendances.count { it.status == AttendanceStatus.Late }
            val total = present + absent + late
            val percent = if (total > 0) ((present.toFloat() / total) * 100).roundToInt() else 0

            state.value = state.value.copy(
                presentCount = present,
                absentCount = absent,
                lateCount = late,
                attendancePercent = percent
            )
        } catch (e: Exception) {
            // Si hay error calculando estadísticas, no afecta la carga principal
            state.value = state.value.copy(
                presentCount = 0,
                absentCount = 0,
                lateCount = 0,
                attendancePercent = 0
            )
        }
    }

    //Cierra sesión y redirige a la pantalla de login
    private fun logout(navigate: (AppDestination) -> Unit) {
        viewModelScope.launch {
            // Limpia el estado y llama al caso de uso de logout
            state.value = TeacherContract.State()
            logoutUseCase()
            navigate(Login)
        }
    }

    //Para abrir el menu
    private fun openDrawer() {
        state.value = state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Open))
    }

    //Para cerrar el menu
    private fun closeDrawer() {
        state.value = state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Closed))
    }

    // Abrir diálogo de crear curso
    private fun openCreateCourseDialog() {
        state.value = state.value.copy(
            showCreateCourseDialog = true,
            createCourseName = "",
            createCourseDescription = "",
            createCourseError = ""
        )
    }

    // Cerrar diálogo de crear curso
    private fun closeCreateCourseDialog() {
        state.value = state.value.copy(
            showCreateCourseDialog = false,
            createCourseName = "",
            createCourseDescription = "",
            createCourseError = "",
            createCourseLoading = false
        )
    }

    // Actualizar nombre del curso
    private fun updateCourseName(name: String) {
        state.value = state.value.copy(createCourseName = name)
    }

    // Actualizar descripción del curso
    private fun updateCourseDescription(description: String) {
        state.value = state.value.copy(createCourseDescription = description)
    }

    // Crear curso
    private fun createCourse() {
        viewModelScope.launch {
            state.value = state.value.copy(createCourseLoading = true, createCourseError = "")

            val teacherId = currentUserUseCase()
            if (teacherId == null) {
                state.value = state.value.copy(
                    createCourseLoading = false,
                    createCourseError = "Sesión no encontrada"
                )
                return@launch
            }

            val courseName = state.value.createCourseName.trim()
            if (courseName.isBlank()) {
                state.value = state.value.copy(
                    createCourseLoading = false,
                    createCourseError = "El nombre del curso es requerido"
                )
                return@launch
            }

            // Crear objeto Course
            val newCourse = Course(
                id = "", // Firebase generará el ID automáticamente
                name = courseName,
                description = state.value.createCourseDescription.trim(),
                teacherId = teacherId
            )

            when (val result = createCourseUseCase(newCourse)) {
                is Result.Ok -> {
                    if (result.value) {
                        // Éxito, cerrar diálogo y recargar cursos
                        state.value = state.value.copy(
                            showCreateCourseDialog = false,
                            createCourseName = "",
                            createCourseDescription = "",
                            createCourseLoading = false,
                            createCourseError = ""
                        )
                        loadTeacherData() // Recargar para mostrar el nuevo curso
                    } else {
                        state.value = state.value.copy(
                            createCourseLoading = false,
                            createCourseError = "No se pudo crear el curso"
                        )
                    }
                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        createCourseLoading = false,
                        createCourseError = result.throwable.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    // Abrir diálogo para vincular etiqueta NFC
    private fun openLinkNfcTagDialog() {
        state.value = state.value.copy(
            showLinkNfcTagDialog = true,
            nfcTagIdInput = "",
            linkNfcTagError = "",
            linkNfcTagSuccess = ""
        )
    }

    // Cerrar diálogo de vinculación de etiqueta NFC
    private fun closeLinkNfcTagDialog() {
        state.value = state.value.copy(
            showLinkNfcTagDialog = false,
            nfcTagIdInput = "",
            linkNfcTagError = "",
            linkNfcTagSuccess = ""
        )
    }

    // Actualizar ID de la etiqueta NFC
    private fun updateNfcTagId(id: String) {
        state.value = state.value.copy(nfcTagIdInput = id)
    }

    // Vincular etiqueta NFC al usuario
    private fun linkNfcTag() {
        viewModelScope.launch {
            state.value = state.value.copy(linkNfcTagLoading = true, linkNfcTagError = "", linkNfcTagSuccess = "")

            val teacherId = currentUserUseCase()
            if (teacherId == null) {
                state.value = state.value.copy(
                    linkNfcTagLoading = false,
                    linkNfcTagError = "Sesión no encontrada"
                )
                return@launch
            }

            val nfcTagId = state.value.nfcTagIdInput.trim()
            if (nfcTagId.isBlank()) {
                state.value = state.value.copy(
                    linkNfcTagLoading = false,
                    linkNfcTagError = "El ID de la etiqueta NFC es requerido"
                )
                return@launch
            }

            when (val result = updateUserNfcTagUseCase(teacherId, nfcTagId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(
                        linkNfcTagLoading = false,
                        linkNfcTagSuccess = "NFC Tag vinculado exitosamente",
                        linkNfcTagError = ""
                    )
                    // Recargar datos del usuario para actualizar el tag actual
                    loadTeacherData()
                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        linkNfcTagLoading = false,
                        linkNfcTagError = result.throwable.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}
