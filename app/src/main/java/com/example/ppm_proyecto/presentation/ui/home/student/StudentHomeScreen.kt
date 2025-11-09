package com.example.ppm_proyecto.presentation.ui.home.student

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.user.Notification
import com.example.ppm_proyecto.presentation.components.AppNavigationDrawer
import com.example.ppm_proyecto.presentation.components.HomeTopBar
import com.example.ppm_proyecto.presentation.components.StatisticsCard
import com.example.ppm_proyecto.presentation.components.LoadingOverlay
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import java.text.SimpleDateFormat
import java.util.Locale


/*============================================
Pantalla de inicio para estudiantes
==============================================*/
@Composable
fun StudentHomeScreen(
    viewModel: StudentHomeViewModel = hiltViewModel(),
    onNavigate: (AppDestination) -> Unit,
) {
    val state by viewModel.state

    // Cargar datos al entrar en la pantalla (si aún no se cargaron)
    LaunchedEffect(Unit) {
        viewModel.ensureLoaded()
    }

    ModalNavigationDrawer(
        drawerState = state.isDrawerOpen,
        drawerContent = {
            AppNavigationDrawer(
                drawerState = state.isDrawerOpen,
                onNavigateToProfile = { viewModel.onIntent(StudentContract.Intent.OpenProfile, onNavigate) },
                onNavigateToSecurity = { viewModel.onIntent(StudentContract.Intent.OpenSecuritySettings, onNavigate) },
                onNavigateToAppearance = { viewModel.onIntent(StudentContract.Intent.OpenAppearanceSettings, onNavigate) },
                onNavigateToLogin = { viewModel.onIntent(StudentContract.Intent.Logout, onNavigate) },
                onCloseDrawer = { viewModel.onIntent(StudentContract.Intent.CloseDrawer, onNavigate) }
            )
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    username = state.user?.name ?: "---",
                    userRoleText = "Estudiante",
                    profilePictureUrl = state.user?.profileImageUrl ?: "",
                    onOpenDrawer = {
                        viewModel.onIntent(StudentContract.Intent.ToggleDrawer, onNavigate)
                    }
                )
            }
        ) { innerPadding ->
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                ) {

                    StatisticsCard(
                        presentCount = state.presentCount,
                        absentCount = state.absentCount,
                        lateCount = state.lateCount,
                        attendancePercent = state.attendancePercent,
                        title = "Estadísticas de Asistencia",
                        modifier = Modifier.padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sección: Notificaciones
                    NotificationsList(
                        notifications = state.notifications,
                        isLoading = state.isLoading,
                        loaded = state.loaded
                    ) { notificationId ->
                        viewModel.onIntent(StudentContract.Intent.ViewNotification(notificationId), onNavigate)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sección: Cursos asignados
                    CoursesList(
                        courses = state.courses,
                        isLoading = state.isLoading,
                        loaded = state.loaded
                    )
                }
                if (state.isLoading || !state.loaded) {
                    LoadingOverlay()
                }
            }
        }
    }
}

@Composable
fun NotificationsList(
    notifications: List<Notification>,
    isLoading: Boolean,
    loaded: Boolean,
    onClick: (String) -> Unit,
) {
    Text(
        text = "Notificaciones",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    if (notifications.isEmpty() && loaded && !isLoading) {
        // Placeholder cuando no hay notificaciones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Estás al día, no hay notificaciones",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    if (notifications.isNotEmpty()) {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(notifications) { noti ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onClick(noti.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Format Firebase Timestamp to a readable date
                        Text(text = dateFormatter.format(noti.date.toDate()), style = MaterialTheme.typography.bodySmall)
                        Text(text = noti.title, style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = noti.message, style = MaterialTheme.typography.bodySmall)

                    }
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun CoursesList(
    courses: List<Course>,
    isLoading: Boolean,
    loaded: Boolean,
) {
    Text(
        text = "Mis cursos",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    if (courses.isEmpty() && loaded && !isLoading) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "No estás asignado a ningún curso",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(courses) { course ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = course.name, style = MaterialTheme.typography.titleSmall)
                    if (course.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = course.description, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            HorizontalDivider()
        }
    }
}

@Preview
@Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun StudentHomeScreenPreview() {
    StudentHomeScreen(
        onNavigate = {}
    )

}