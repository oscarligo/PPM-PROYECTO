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
import com.example.ppm_proyecto.domain.usecase.student.GetStudentDataUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentCoursesUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentAttendanceUseCase
import kotlinx.coroutines.launch
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.data.local.sample.sampleNotifications



@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val getStudentDataUseCase: GetStudentDataUseCase,
    private val getStudentCoursesUseCase: GetStudentCoursesUseCase,
    private val getStudentAttendanceUseCase: GetStudentAttendanceUseCase,
)


    : ViewModel() {
    var state = mutableStateOf(StudentContract.State())

    init {
        loadStudentData()
    }

    fun onIntent(intent: StudentContract.Intent, navigate: (AppDestination) -> Unit) {
        when (intent) {
            is StudentContract.Intent.SeeCourseDetails -> navigate(CourseDetails(intent.courseId))
            StudentContract.Intent.OpenProfile -> navigate(Profile)
            StudentContract.Intent.OpenSecuritySettings -> navigate(SecuritySettings)
            StudentContract.Intent.OpenAppearanceSettings -> navigate(AppearanceSettings)
            is StudentContract.Intent.ViewNotification -> state.value = state.value.copy(selectedNotification = intent.notificationId)
            StudentContract.Intent.ToggleDrawer -> openDrawer()
            StudentContract.Intent.CloseDrawer -> closeDrawer()
        }
    }

    private fun loadStudentData() {
        // Use a fixed sample student id for demo / local data. In a real app you'd get this from auth.
        val studentId = "stu-1"

        viewModelScope.launch {
            // Indicate loading
            state.value = state.value.copy(isLoading = true, error = "")

            // 1) Get basic user data
            when (val res = getStudentDataUseCase(studentId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(user = res.value)
                }
                is Result.Err -> {
                    state.value = state.value.copy(error = "Error cargando datos de usuario: ${res.throwable.message}")
                }
            }

            // 2) Get courses
            when (val resCourses = getStudentCoursesUseCase(studentId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(courses = resCourses.value)
                }
                is Result.Err -> {
                    val prev = state.value
                    state.value = prev.copy(error = "Error cargando cursos: ${resCourses.throwable.message}")
                }
            }

            // 3) Obtener asistencias del estudiante por curso y calcular estadísticas
            try {
                val allAttendances = mutableListOf<SessionAttendance>()
                state.value.courses.forEach { course ->
                    when (val attRes = getStudentAttendanceUseCase(course.id, studentId)) {
                        is Result.Ok -> allAttendances.addAll(attRes.value)
                        is Result.Err -> { /* ignorar error puntual de curso */ }
                    }
                }
                calculateStats(allAttendances)
            } catch (t: Throwable) {
                state.value = state.value.copy(error = "Error procesando asistencias: ${t.message}")
            }

            // 4) Cargar notificaciones locales de muestra para pruebas
            state.value = state.value.copy(notifications = sampleNotifications)

            // Done loading
            state.value = state.value.copy(isLoading = false)
        }
    }


    /**
     * Calcula las estadísticas de asistencia a partir de los registros proporcionados.
     */
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


/**
 * Abre el menú lateral de navegación.
 */

    private fun openDrawer() {
        state.value = state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Open))
    }

    private fun closeDrawer() {
        state.value = state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Closed))
    }
}
