package com.example.ppm_proyecto.presentation.ui.home.student

import androidx.lifecycle.ViewModel

import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.AppearanceSettings
import com.example.ppm_proyecto.presentation.navigation.routes.CourseDetails
import com.example.ppm_proyecto.presentation.navigation.routes.SecuritySettings
import com.example.ppm_proyecto.presentation.navigation.routes.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import kotlin.math.roundToInt
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.viewModelScope
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.ppm_proyecto.domain.usecase.student.GetStudentDataUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentCoursesUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetCourseSessionsUseCase
import kotlinx.coroutines.launch


@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val getStudentDataUseCase: GetStudentDataUseCase,
    private val getStudentCoursesUseCase: GetStudentCoursesUseCase,
    private val getStudentAttendanceUseCase: GetStudentAttendanceUseCase,
    private val getCourseSessionsUseCase: GetCourseSessionsUseCase,
)


    : ViewModel() {
    private val _state = MutableStateFlow(StudentHomeState())
    val state = _state.asStateFlow()

    private val studentId = "stu-1"

    init {
        loadStudentData()
    }

    fun onIntent(intent: StudentHomeIntent, navigate: (AppDestination) -> Unit) {
        when (intent) {
            is StudentHomeIntent.SeeCourseDetails -> navigate(CourseDetails(intent.courseId))
            StudentHomeIntent.OpenProfile -> navigate(Profile)
            StudentHomeIntent.OpenSecuritySettings -> navigate(SecuritySettings)
            StudentHomeIntent.OpenAppearanceSettings -> navigate(AppearanceSettings)
            StudentHomeIntent.ToggleDrawer -> openDrawer()
            StudentHomeIntent.CloseDrawer -> closeDrawer()

            is StudentHomeIntent.ViewNotification -> {
                _state.value = _state.value.copy(selectedNotification = intent.notificationId)
            }

        }
    }

    private fun loadStudentData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val student = getStudentDataUseCase(studentId)

            val courses = getStudentCoursesUseCase(studentId)

            val allAttendances = mutableListOf<SessionAttendance>()
            for (course in courses) {
                val attendance = getStudentAttendanceUseCase(course.id, studentId)
                allAttendances.addAll(attendance)
            }

            val stats = calculateStats(allAttendances)

            _state.value = _state.value.copy(
                isLoading = false,
                student = student,
                courses = courses,
                attendanceRecords = allAttendances,
                presentCount = stats.present,
                absentCount = stats.absent,
                lateCount = stats.late,
                attendancePercent = stats.percent,
                barItems = stats.barItems
            )
        }
    }


    private fun calculateStats(attendanceRecords: List<SessionAttendance>): Unit {
        // Contar los estados
        val present = attendanceRecords.count { it.status == AttendanceStatus.Present.name }
        val absent = attendanceRecords.count { it.status == AttendanceStatus.Absent.name }
        val late = attendanceRecords.count { it.status == AttendanceStatus.Late.name }

        // Calcular totales y porcentaje
        val total = present + absent + late
        val percent = if (total > 0) ((present.toFloat() / total) * 100).roundToInt() else 0

        // Preparar datos para un gráfico o barra visual
        val bars = listOf(
            "Ausente" to absent.toFloat(),
            "Tarde" to late.toFloat(),
            "Presente" to present.toFloat(),
        )

        _state.value = _state.value.copy(
            presentCount = present,
            absentCount = absent,
            lateCount = late,
            attendancePercent = percent,
            barItems = bars
        )
    }




    private fun openDrawer() {
        _state.value = _state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Open))
    }

    private fun closeDrawer() {
        _state.value = _state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Closed))
    }
}
