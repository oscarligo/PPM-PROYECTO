package com.example.ppm_proyecto.presentation.ui.home.student

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ppm_proyecto.presentation.components.AppNavigationDrawer
import com.example.ppm_proyecto.presentation.components.HomeTopBar
import com.example.ppm_proyecto.presentation.components.StatisticsCard
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import kotlinx.coroutines.flow.collectLatest


/*============================================
Pantalla de inicio para estudiantes
==============================================*/
@Composable
fun StudentHomeScreen(
    viewModel: StudentHomeViewModel = StudentHomeViewModel(),
    onNavigate: (AppDestination) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Sincroniza el estado del ViewModel con el Drawer real
    LaunchedEffect(state.isDrawerOpen) {
        if (state.isDrawerOpen) drawerState.open() else drawerState.close()
    }

    // Sincroniza cierre por gesto con el ViewModel
    LaunchedEffect(drawerState) {
        snapshotFlow { drawerState.isOpen }.collectLatest { isOpen ->
            if (!isOpen && state.isDrawerOpen) {
                viewModel.onIntent(StudentHomeIntent.CloseDrawer, onNavigate)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawer(
                drawerState = drawerState,
                onNavigateToProfile = { viewModel.onIntent(StudentHomeIntent.OpenProfile, onNavigate) },
                onNavigateToSecurity = { viewModel.onIntent(StudentHomeIntent.OpenSecuritySettings, onNavigate) },
                onNavigateToAppearance = { viewModel.onIntent(StudentHomeIntent.OpenAppearanceSettings, onNavigate) },
                onCloseDrawer = { viewModel.onIntent(StudentHomeIntent.CloseDrawer, onNavigate) }
            )
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    userRoleText = "Estudiante",
                    profilePictureUrl = state.user?.profileImageUrl ?: "",
                    onOpenDrawer = {
                        viewModel.onIntent(StudentHomeIntent.ToggleDrawer, onNavigate)
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {

                // Seccion: Estadísticas de asistencia

                StatisticsCard(
                    title = "Estadísticas de asistencia",
                    isLoading = state.isLoading,
                    errorMessage = state.error.takeIf { it.isNotBlank() },
                    presentCount = state.presentCount,
                    absentCount = state.absentCount,
                    lateCount = state.lateCount,
                    attendancePercent = state.attendancePercent,
                    barItems = state.barItems,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sección: Cursos asignados

                Text(
                    text = "Mis cursos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    items(state.courses) { course ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { viewModel.onIntent(StudentHomeIntent.SeeCourseDetails(course.id), onNavigate) }
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