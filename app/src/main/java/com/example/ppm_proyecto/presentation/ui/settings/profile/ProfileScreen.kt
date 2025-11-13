package com.example.ppm_proyecto.presentation.ui.settings.profile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.ppm_proyecto.domain.models.user.UserRole
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.ensureLoaded()
    }

    LaunchedEffect(true) {
        if (state.loaded) {
            viewModel.onIntent(ProfileContract.Intent.RefreshUserData)
        }
    }

    LaunchedEffect(state.successMessage) {
        if (state.successMessage.isNotEmpty()) {
            Toast.makeText(context, state.successMessage, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(state.error) {
        if (state.error.isNotEmpty()) {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        }
    }

    // Nuevo: Manejar evento de copiar al portapapeles desde el estado
    LaunchedEffect(state.clipboardText) {
        state.clipboardText?.let { text ->
            copyToClipboard(context, text)
            Toast.makeText(context, "Copiado al portapapeles", Toast.LENGTH_SHORT).show()
            viewModel.onIntent(ProfileContract.Intent.ClearClipboardEvent)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading && state.user == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.user != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image Section
                    ProfileImageSection(
                        imageUrl = state.profileImageUrl,
                        showDialog = state.showImageUrlDialog,
                        tempUrl = state.tempImageUrl,
                        onOpenDialog = { viewModel.onIntent(ProfileContract.Intent.OpenImageUrlDialog) },
                        onCloseDialog = { viewModel.onIntent(ProfileContract.Intent.CloseImageUrlDialog) },
                        onTempUrlChange = { viewModel.onIntent(ProfileContract.Intent.SetTempImageUrl(it)) },
                        onSaveUrl = { viewModel.onIntent(ProfileContract.Intent.SaveImageUrl) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Editable Name Field
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { viewModel.onIntent(ProfileContract.Intent.SetName(it)) },
                        label = { Text("Nombre") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Read-only fields with copy functionality
                    ReadOnlyFieldWithCopy(
                        label = "ID de Usuario",
                        value = state.user?.id ?: "",
                        icon = Icons.Default.AccountCircle,
                        onCopy = { copyToClipboard(context, it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ReadOnlyFieldWithCopy(
                        label = "Rol",
                        value = when (state.user?.role) {
                            UserRole.Student -> "Estudiante"
                            UserRole.Teacher -> "Profesor"
                            else -> "Desconocido"
                        },
                        icon = Icons.Default.Person,
                        onCopy = { copyToClipboard(context, it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ReadOnlyFieldWithCopy(
                        label = "Fecha de Creación",
                        value = formatDate(state.user?.createdAt?.toDate()),
                        icon = Icons.Default.DateRange,
                        onCopy = { copyToClipboard(context, it) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Save Button
                    Button(
                        onClick = { viewModel.onIntent(ProfileContract.Intent.SaveChanges) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !state.isLoading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardar Cambios", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileImageSection(
    imageUrl: String,
    showDialog: Boolean,
    tempUrl: String,
    onOpenDialog: () -> Unit,
    onCloseDialog: () -> Unit,
    onTempUrlChange: (String) -> Unit,
    onSaveUrl: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Sin foto de perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edit Image URL Button
        OutlinedButton(
            onClick = onOpenDialog,
            modifier = Modifier.wrapContentWidth()
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cambiar URL de Foto")
        }
    }

    // Dialog to edit image URL
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onCloseDialog,
            title = { Text("URL de Foto de Perfil") },
            text = {
                OutlinedTextField(
                    value = tempUrl,
                    onValueChange = onTempUrlChange,
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = onSaveUrl) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = onCloseDialog) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ReadOnlyFieldWithCopy(
    label: String,
    value: String,
    icon: ImageVector,
    onCopy: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(onClick = { onCopy(value) }) {
                Icon(
                    Icons.Default.MailOutline,
                    contentDescription = "Copiar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}

private fun formatDate(date: Date?): String {
    if (date == null) return "Desconocida"
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(date)
}
