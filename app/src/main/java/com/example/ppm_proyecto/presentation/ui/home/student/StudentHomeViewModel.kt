package com.example.ppm_proyecto.presentation.ui.home.student

import androidx.lifecycle.ViewModel
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.AppearanceSettings
import com.example.ppm_proyecto.presentation.navigation.routes.CourseDetails
import com.example.ppm_proyecto.presentation.navigation.routes.SecuritySettings
import com.example.ppm_proyecto.presentation.navigation.routes.Profile
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import kotlin.math.roundToInt
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.ppm_proyecto.domain.usecase.student.GetStudentCoursesUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.student.EnrollInCourseUseCase
import kotlinx.coroutines.launch
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.usecase.auth.LogoutUseCase
import com.example.ppm_proyecto.presentation.navigation.routes.Login
import com.example.ppm_proyecto.domain.usecase.auth.CurrentUserUseCase
import com.example.ppm_proyecto.domain.usecase.user.GetUserUseCase
import com.example.ppm_proyecto.domain.usecase.user.GetStudentNotificationsUseCase


@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val getStudentCoursesUseCase: GetStudentCoursesUseCase,
    private val getStudentAttendanceUseCase: GetStudentAttendanceUseCase,
    private val enrollInCourseUseCase: EnrollInCourseUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getStudentNotificationsUseCase: GetStudentNotificationsUseCase,
) : ViewModel() {
    var state = mutableStateOf(StudentContract.State())
        private set

    // Cargar datos solo si no se han cargado
    fun ensureLoaded() {
        if (!state.value.loaded && !state.value.isLoading) {
            loadStudentData()
        }
    }

    fun onIntent(intent: StudentContract.Intent, navigate: (AppDestination) -> Unit) {
        when (intent) {
            is StudentContract.Intent.SeeCourseDetails -> navigate(CourseDetails(intent.courseId))
            StudentContract.Intent.OpenProfile -> navigate(Profile)
            StudentContract.Intent.OpenSecuritySettings -> navigate(SecuritySettings)
            StudentContract.Intent.OpenAppearanceSettings -> navigate(AppearanceSettings)
            StudentContract.Intent.Logout -> {
                logout(navigate)
            }
            is StudentContract.Intent.ViewNotification -> state.value = state.value.copy(selectedNotification = intent.notificationId)
            StudentContract.Intent.ToggleDrawer -> openDrawer()
            StudentContract.Intent.CloseDrawer -> closeDrawer()
            StudentContract.Intent.RefreshUserData -> loadStudentData()
            StudentContract.Intent.OpenJoinCourseDialog -> openJoinCourseDialog()
            StudentContract.Intent.CloseJoinCourseDialog -> closeJoinCourseDialog()
            is StudentContract.Intent.UpdateJoinCourseId -> updateJoinCourseId(intent.courseId)
            StudentContract.Intent.JoinCourse -> joinCourse()
        }
    }

    private fun loadStudentData() {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = "")
            val studentId = currentUserUseCase()
            if (studentId == null) {
                state.value = state.value.copy(isLoading = false, error = "Sesión no encontrada")
                return@launch
            }
            when (val res = getUserUseCase(studentId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(user = res.value)
                }
                is Result.Err -> {
                    state.value = state.value.copy(error = "Error cargando datos de usuario: ${res.throwable.message}")
                }
            }
            when (val resCourses = getStudentCoursesUseCase(studentId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(courses = resCourses.value)
                }
                is Result.Err -> {
                    state.value = state.value.copy(error = "Error cargando cursos: ${resCourses.throwable.message}")
                }
            }
            try {
                val allAttendances = mutableListOf<SessionAttendance>()
                state.value.courses.forEach { course ->
                    when (val attRes = getStudentAttendanceUseCase(course.id, studentId)) {
                        is Result.Ok -> allAttendances.addAll(attRes.value)
                        is Result.Err -> { /* ignorar error puntual */ }
                    }
                }
                calculateStats(allAttendances)
            } catch (t: Throwable) {
                state.value = state.value.copy(error = "Error procesando asistencias: ${t.message}")
            }

            // Notificaciones
            when (val notifRes = getStudentNotificationsUseCase(studentId)) {
                is Result.Ok -> {
                    val list = notifRes.value
                    state.value = state.value.copy(notifications = list)
                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        error = "Error cargando notificaciones: ${notifRes.throwable.message}",
                    )
                }
            }

            state.value = state.value.copy(isLoading = false, loaded = true)
        }
    }

    private fun logout(navigate: (AppDestination) -> Unit) {
        viewModelScope.launch {
            state.value = StudentContract.State() // Reset total
            logoutUseCase()
            navigate(Login)
        }
    }

    private fun calculateStats(attendanceRecords: List<SessionAttendance>) {
        val present = attendanceRecords.count { it.status == AttendanceStatus.Present.name }
        val absent = attendanceRecords.count { it.status == AttendanceStatus.Absent.name }
        val late = attendanceRecords.count { it.status == AttendanceStatus.Late.name }
        val total = present + absent + late
        val percent = if (total > 0) ((present.toFloat() / total) * 100).roundToInt() else 0
        state.value = state.value.copy(
            presentCount = present,
            absentCount = absent,
            lateCount = late,
            attendancePercent = percent,
        )
    }

    private fun openDrawer() {
        state.value = state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Open))
    }

    private fun closeDrawer() {
        state.value = state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Closed))
    }

    private fun openJoinCourseDialog() {
        state.value = state.value.copy(
            showJoinCourseDialog = true,
            joinCourseId = "",
            joinCourseError = ""
        )
    }

    private fun closeJoinCourseDialog() {
        state.value = state.value.copy(
            showJoinCourseDialog = false,
            joinCourseId = "",
            joinCourseError = "",
            joinCourseLoading = false
        )
    }

    private fun updateJoinCourseId(courseId: String) {
        state.value = state.value.copy(joinCourseId = courseId)
    }

    private fun joinCourse() {
        viewModelScope.launch {
            state.value = state.value.copy(joinCourseLoading = true, joinCourseError = "")
            val studentId = currentUserUseCase()
            if (studentId == null) {
                state.value = state.value.copy(
                    joinCourseLoading = false,
                    joinCourseError = "Sesión no encontrada"
                )
                return@launch
            }

            val courseId = state.value.joinCourseId.trim()
            if (courseId.isBlank()) {
                state.value = state.value.copy(
                    joinCourseLoading = false,
                    joinCourseError = "Debes ingresar un ID de curso"
                )
                return@launch
            }

            when (val result = enrollInCourseUseCase(studentId, courseId)) {
                is Result.Ok -> {
                    if (result.value) {
                        // Éxito, cerrar diálogo y recargar cursos
                        state.value = state.value.copy(
                            showJoinCourseDialog = false,
                            joinCourseId = "",
                            joinCourseLoading = false,
                            joinCourseError = ""
                        )
                        loadStudentData() // Recargar para mostrar el nuevo curso
                    } else {
                        state.value = state.value.copy(
                            joinCourseLoading = false,
                            joinCourseError = "No se pudo unir al curso"
                        )
                    }
                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        joinCourseLoading = false,
                        joinCourseError = result.throwable.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}
