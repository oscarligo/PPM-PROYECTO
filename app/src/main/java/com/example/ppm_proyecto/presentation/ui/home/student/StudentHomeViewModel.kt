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
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class StudentHomeViewModel: ViewModel() {
    private val _state = MutableStateFlow(StudentHomeState())
    val state = _state.asStateFlow()

    init {
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

        }
    }

    private fun calculateStats() {
        val attendanceRecords = sampleStudent.attendanceRecords
        val present = attendanceRecords.count { it.status == AttendanceStatus.PRESENT }
        val absent = attendanceRecords.count { it.status == AttendanceStatus.ABSENT }
        val late = attendanceRecords.count { it.status == AttendanceStatus.LATE }

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
        _state.value = _state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Open))
    }

    private fun closeDrawer() {
        _state.value = _state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Closed))
    }
}
