package com.example.ppm_proyecto.presentation.ui.coursedetails.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.presentation.components.AppNavigationDrawer
import com.example.ppm_proyecto.presentation.components.HomeTopBar
import com.example.ppm_proyecto.presentation.components.LoadingOverlay
import com.example.ppm_proyecto.presentation.components.StatisticsCard
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.AppearanceSettings
import com.example.ppm_proyecto.presentation.navigation.routes.Login
import com.example.ppm_proyecto.presentation.navigation.routes.Profile
import com.example.ppm_proyecto.presentation.navigation.routes.SecuritySettings
import com.example.ppm_proyecto.presentation.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Pantalla de detalles del curso para estudiantes
 * Muestra estadísticas de asistencia e historial
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsStudentScreen(
    courseId: String,
    studentId: String,
    onNavigate: (AppDestination) -> Unit,
    viewModel: CourseDetailsStudentViewModel = hiltViewModel(),

) {
    val state by viewModel.state.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cargar datos cuando se monta la pantalla
    LaunchedEffect(courseId, studentId) {
        viewModel.handleIntent(
            CourseDetailsStudentIntent.LoadCourseDetails(courseId, studentId)
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawer(
                drawerState = drawerState,
                onNavigateToProfile = { onNavigate(Profile) },
                onNavigateToSecurity = { onNavigate(SecuritySettings) },
                onNavigateToAppearance = { onNavigate(AppearanceSettings) },
                onNavigateToLogin = { onNavigate(Login) },
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    username = state.userName,
                    userRoleText = "Estudiante",
                    profilePictureUrl = state.userProfileUrl,
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Contenido principal
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    if (state.error != null) {
                        // Mostrar error
                        item {
                            ErrorSection(
                                error = state.error!!,
                                onRetry = {
                                    viewModel.handleIntent(CourseDetailsStudentIntent.RetryLoading)
                                }
                            )
                        }
                    } else if (!state.isLoading) {
                        // Título del curso
                        item {
                            state.course?.let { course ->
                                Text(
                                    text = course.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        // Sección de Estadísticas usando el componente reutilizable
                        item {
                            StatisticsCard(
                                presentCount = state.presentCount,
                                absentCount = state.absentCount,
                                lateCount = state.lateCount,
                                attendancePercent = state.attendancePercent,
                                title = "Estadísticas de Asistencia"
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                "Historial de Asistencia",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Lista de Asistencias
                        items(state.attendanceRecords) { record ->
                            AttendanceCardFromFirebase(record = record)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Mensaje si no hay registros
                        if (state.attendanceRecords.isEmpty()) {
                            item {
                                EmptyStateSection()
                            }
                        }
                    }
                }

                // Loading Overlay
                if (state.isLoading) {
                    LoadingOverlay()
                }
            }
        }
    }
}

/**
 * Card de asistencia que usa los datos de Firebase
 */
@Composable
private fun AttendanceCardFromFirebase(record: SessionAttendance) {
    val status = when (record.status.lowercase()) {
        "present" -> AttendanceStatus.Present
        "absent" -> AttendanceStatus.Absent
        "late" -> AttendanceStatus.Late
        else -> AttendanceStatus.Absent
    }

    val backgroundColor = when (status) {
        AttendanceStatus.Present -> StatusPresentGreen
        AttendanceStatus.Absent -> StatusAbsentRed
        AttendanceStatus.Late -> StatusWarningYellow
    }
    val contentColor = when (status) {
        AttendanceStatus.Present -> StatusPresentGreenText
        AttendanceStatus.Absent -> StatusAbsentRedText
        AttendanceStatus.Late -> StatusWarningYellowText
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Sesión: ${record.sessionId}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                record.entryTime?.let { timestamp ->
                    val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(timestamp.toDate())
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = status.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

/**
 * Sección de error con botón de reintentar
 */
@Composable
private fun ErrorSection(error: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

/**
 * Sección de estado vacío cuando no hay registros
 */
@Composable
private fun EmptyStateSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "No hay registros de asistencia",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Los registros aparecerán aquí cuando se marquen asistencias",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseDetailsLoadingPreview() {
    PPMPROYECTOTheme {
        CourseDetailsStudentScreen(
            courseId = "1",
            studentId = "1",
            onNavigate = {}

        )
    }
}