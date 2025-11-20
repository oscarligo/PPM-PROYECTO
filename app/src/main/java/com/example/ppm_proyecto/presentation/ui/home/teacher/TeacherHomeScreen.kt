package com.example.ppm_proyecto.presentation.ui.home.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.presentation.components.AppNavigationDrawer
import com.example.ppm_proyecto.presentation.components.HomeTopBar
import com.example.ppm_proyecto.presentation.components.LoadingOverlay
import com.example.ppm_proyecto.presentation.components.AppAB
import com.example.ppm_proyecto.presentation.components.CreateCourseDialog
import com.example.ppm_proyecto.presentation.components.LinkNfcTagDialog
import com.example.ppm_proyecto.presentation.navigation.routes.*
import com.example.ppm_proyecto.presentation.theme.PPMPROYECTOTheme
import kotlinx.coroutines.launch

/*============================================
Pantalla de inicio para profesores
==============================================*/
@Composable
fun TeacherHomeScreen(
    viewModel: TeacherHomeViewModel = hiltViewModel(),
    onNavigate: (AppDestination) -> Unit,
) {
    val state by viewModel.state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cargar datos al entrar en la pantalla (si aún no se cargaron)
    LaunchedEffect(Unit) {
        viewModel.ensureLoaded()
    }

    // Refrescar datos del usuario cada vez que esta pantalla se vuelve visible
    LaunchedEffect(true) {
        if (state.loaded) {
            viewModel.onIntent(TeacherContract.Intent.RefreshData, onNavigate)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawer(
                drawerState = drawerState,
                onNavigateToProfile = { viewModel.onIntent(TeacherContract.Intent.OpenProfile, onNavigate) },
                onNavigateToSecurity = { viewModel.onIntent(TeacherContract.Intent.OpenSecuritySettings, onNavigate) },
                onNavigateToAppearance = { viewModel.onIntent(TeacherContract.Intent.OpenAppearanceSettings, onNavigate) },
                onNavigateToLogin = { viewModel.onIntent(TeacherContract.Intent.Logout, onNavigate) },
                onCloseDrawer = { viewModel.onIntent(TeacherContract.Intent.CloseDrawer, onNavigate) }
            )
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    username = state.user?.name ?: "Profesor",
                    userRoleText = "Profesor",
                    profilePictureUrl = state.user?.profileImageUrl ?: "",
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onLinkNfcTag = { viewModel.onIntent(TeacherContract.Intent.OpenLinkNfcTagDialog, onNavigate) }
                )
            },
            floatingActionButton = {
                AppAB(
                    onClick = { viewModel.onIntent(TeacherContract.Intent.OpenCreateCourseDialog, onNavigate) },
                    text = "Crear curso"
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sección de Cursos
                    Text(
                        text = "Mis Cursos",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    if (state.courses.isEmpty() && state.loaded && !state.isLoading) {
                        // Placeholder cuando no hay cursos
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
                                        text = "No tienes cursos creados",
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Usa el botón + para crear tu primer curso",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        CoursesList(
                            courses = state.courses,
                            onCourseClick = { courseId ->
                                viewModel.onIntent(TeacherContract.Intent.OpenCourseDetails(courseId), onNavigate)
                            }
                        )
                    }
                }

                // Loading Overlay
                if (state.isLoading || !state.loaded) {
                    LoadingOverlay()
                }
            }
        }

        // Diálogo para crear un curso
        CreateCourseDialog(
            showDialog = state.showCreateCourseDialog,
            courseName = state.createCourseName,
            courseDescription = state.createCourseDescription,
            onCourseNameChange = { viewModel.onIntent(TeacherContract.Intent.UpdateCourseName(it), onNavigate) },
            onCourseDescriptionChange = { viewModel.onIntent(TeacherContract.Intent.UpdateCourseDescription(it), onNavigate) },
            onDismiss = { viewModel.onIntent(TeacherContract.Intent.CloseCreateCourseDialog, onNavigate) },
            onConfirm = { viewModel.onIntent(TeacherContract.Intent.CreateCourse, onNavigate) },
            isLoading = state.createCourseLoading,
            errorMessage = state.createCourseError
        )

        // Diálogo para vincular NFC tag
        LinkNfcTagDialog(
            showDialog = state.showLinkNfcTagDialog,
            nfcTagId = state.nfcTagIdInput,
            currentNfcTagId = state.user?.nfcTagId ?: "",
            onNfcTagIdChange = { viewModel.onIntent(TeacherContract.Intent.UpdateNfcTagId(it), onNavigate) },
            onDismiss = { viewModel.onIntent(TeacherContract.Intent.CloseLinkNfcTagDialog, onNavigate) },
            onConfirm = { viewModel.onIntent(TeacherContract.Intent.LinkNfcTag, onNavigate) },
            isLoading = state.linkNfcTagLoading,
            errorMessage = state.linkNfcTagError,
            successMessage = state.linkNfcTagSuccess
        )
    }
}

@Composable
fun CoursesList(
    courses: List<Course>,
    onCourseClick: (String) -> Unit = {}
) {
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
                    .clickable { onCourseClick(course.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = course.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    if (course.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
fun TeacherHomeScreenPreview() {
    PPMPROYECTOTheme {
        TeacherHomeScreen(onNavigate = {})
    }
}
