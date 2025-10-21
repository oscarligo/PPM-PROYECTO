package com.example.ppm_proyecto.presentation.ui.home.student

import androidx.lifecycle.ViewModel
import com.example.ppm_proyecto.data.local.sample.sampleCourseAttendance
import com.example.ppm_proyecto.data.local.sample.sampleStudent
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.AppearanceSettings
import com.example.ppm_proyecto.presentation.navigation.routes.CourseDetails
import com.example.ppm_proyecto.presentation.navigation.routes.SecuritySettings
import com.example.ppm_proyecto.presentation.navigation.routes.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.ppm_proyecto.domain.model.User
import com.example.ppm_proyecto.domain.model.UserRole
import com.example.ppm_proyecto.domain.model.AttendanceStatus
import kotlin.math.roundToInt

class StudentHomeViewModel: ViewModel() {
    private val _state = MutableStateFlow(StudentHomeState())
    val state = _state.asStateFlow()

    private val courseAttendance = sampleCourseAttendance()

    init {
        val courses = courseAttendance.map { it.course }
        val user = User(
            id = "sample-student",
            name = sampleStudent.name,
            role = UserRole.Student,
            profileImageUrl = sampleStudent.profileImageUrl
        )
        _state.value = _state.value.copy(
            user = user,
            userName = sampleStudent.name,
            courses = courses,
        )
        calculateStats()
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
            is StudentHomeIntent.SelectCourse -> {navigate
                if (intent.courseId != null) {
                    navigate(CourseDetails(intent.courseId))
                }

            }
        }
    }

    private fun calculateStats() {
        val present = courseAttendance.sumOf { ca -> ca.records.count { it.status == AttendanceStatus.Presente } }
        val absent = courseAttendance.sumOf { ca -> ca.records.count { it.status == AttendanceStatus.Ausente } }
        val late = courseAttendance.sumOf { ca -> ca.records.count { it.status == AttendanceStatus.Tarde } }

        val total = present + absent + late
        val percent = if (total > 0) ((present.toFloat() / total.toFloat()) * 100f).roundToInt() else 0

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
            barItems = bars,
        )
    }

    private fun openDrawer() {
        _state.value = _state.value.copy(isDrawerOpen = true)
    }

    private fun closeDrawer() {
        _state.value = _state.value.copy(isDrawerOpen = false)
    }
}
