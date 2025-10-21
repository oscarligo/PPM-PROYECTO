// Ubicación: presentation/components/AppNavigationDrawer.kt
package com.example.ppm_proyecto.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ppm_proyecto.presentation.theme.PPMPROYECTOTheme

/*===============================================================
Navigation Drawer reutilizable para toda la aplicación.
Los botones navegan a: Perfil, Seguridad y Apariencia.
Al presionar un botón, navega Y cierra el drawer automáticamente.
=================================================================*/
@Preview
@Composable
fun AppNavigationDrawerPreview() {
    PPMPROYECTOTheme {
        AppNavigationDrawer(
            drawerState = DrawerState(DrawerValue.Open),
            onNavigateToProfile = {},
            onNavigateToSecurity = {},
            onNavigateToAppearance = {},
            onCloseDrawer = {}
        )
    }
}


@Composable
fun AppNavigationDrawer(
    drawerState: DrawerState,
    onNavigateToProfile: () -> Unit,      // Navega a Profile
    onNavigateToSecurity: () -> Unit,     // Navega a SecuritySettings
    onNavigateToAppearance: () -> Unit,   // Navega a AppearanceSettings
    onCloseDrawer: () -> Unit,            // Cierra el drawer
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = modifier.width(280.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
        ) {
            // Header del drawer
            DrawerHeader()

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón: Perfil (navega a Profile)
            DrawerMenuItem(
                icon = Icons.Default.AccountCircle,
                label = "Perfil",
                onClick = {
                    onNavigateToProfile()  // ← Navega a la pantalla Profile
                    onCloseDrawer()        // ← Cierra el drawer
                }
            )

            // Botón: Seguridad (navega a SecuritySettings)
            DrawerMenuItem(
                icon = Icons.Default.Lock,
                label = "Seguridad",
                onClick = {
                    onNavigateToSecurity()  // ← Navega a SecuritySettings
                    onCloseDrawer()         // ← Cierra el drawer
                }
            )

            // Botón: Apariencia (navega a AppearanceSettings)
            DrawerMenuItem(
                icon = Icons.Default.Build,
                label = "Apariencia",
                onClick = {
                    onNavigateToAppearance()  // ← Navega a AppearanceSettings
                    onCloseDrawer()           // ← Cierra el drawer
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Footer opcional
            DrawerFooter()
        }
    }
}

@Composable
private fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Menú",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Configuración y ajustes",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable(onClick = onClick),  // ← Al hacer clic ejecuta onClick
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)  // Color principal suave
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Icono con color principal
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,  // Color principal
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Texto del label
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun DrawerFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Universidad del Valle de Guatemala\n"
                    + "José Rivera\n"
                    + "Oscar Rompich\n"
                    + "Marcela Castillo",

            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
