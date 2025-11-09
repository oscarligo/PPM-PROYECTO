package com.example.ppm_proyecto.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.ppm_proyecto.presentation.theme.PPMPROYECTOTheme
import com.example.ppm_proyecto.R
import com.example.ppm_proyecto.presentation.theme.UniGreenDark


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeTopBar(
    username: String,
    userRoleText: String,
    profilePictureUrl: String,
    onOpenDrawer: () -> Unit,

) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = UniGreenDark,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Abrir ajustes",
                    modifier = Modifier.size(60.dp)
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Spacer( modifier = Modifier.weight(0.3f) )
                Text(
                    text = "$userRoleText / $username",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White

                )

                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = "Foto de perfil",
                    placeholder = painterResource(R.drawable.userholder2),
                    error = painterResource(R.drawable.userholder2),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer( modifier = Modifier.weight(0.00001f) )

            }
        },
    )
}

@Preview
@Composable
fun HomeTopBarPreview() {
    PPMPROYECTOTheme {
        HomeTopBar(
            username = "Juan",
            userRoleText = "Estudiante",
            profilePictureUrl = "",
            onOpenDrawer = {}
        )
    }
}
