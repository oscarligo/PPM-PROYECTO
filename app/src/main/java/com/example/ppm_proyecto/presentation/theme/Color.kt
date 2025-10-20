package com.example.ppm_proyecto.presentation.theme

import androidx.compose.ui.graphics.Color

/** =======================================================
 *  A. COLORES INSTITUCIONALES (Para roles M3):
 *      1. Primary: Elementos clave, acciones principales.
 *      2. Secondary: Elementos secundarios de la interfaz como filtros o acciones relacionadas.
 *      3. Tertiary: Énfasis en información, contrasta a los elementos secundarios.
 *      4. Surface: Fondos de componentes y contentedores.
 *      5. Error: Indica un fallo o problema que requiere una acción.
 * ==========================================================
 */

val UniGreenDark = Color(0xFF056545) //Primary
val UniGreenLight = Color(0xFF83BC41) //Secundary
val UniGreenAccent = Color(0xFF39B24A) //Tertiary
val UniNeutralDark = Color(0xFF414042)//Surface

/**
 * PALETA DE COLORES TEMA CLARO
 */
val LightPrimary = UniGreenDark
val LightOnPrimary = Color.White
val LightPrimaryContainer = UniGreenAccent.copy(alpha = 0.2f) // Un tono más suave
val LightOnPrimaryContainer = UniGreenDark

val LightSecondary = UniGreenLight
val LightOnSecondary = Color.White

val LightTertiary=UniNeutralDark
val LightOnTertiary = Color.White
val LightTertiaryContainer = UniGreenLight.copy(alpha = 0.2f)
val LightOnTertiaryContainer = UniGreenDark

val LightBackGround = Color.White
val LightSurface= Color.White
val LightOnSurface = UniNeutralDark
val LightSurfaceVariant = UniGreenLight.copy(alpha = 0.2f)
val LightOnSurfaceVariant = UniGreenDark

val LightError = Color.Red
val LightOnError = Color.White

/**
 * PALETA DE COLORES TEMA OSCURO
 */
val DarkPrimary = UniGreenLight
val DarkOnPrimary = UniGreenDark
val DarkPrimaryContainer = UniGreenLight.copy(alpha = 0.2f)
val DarkOnPrimaryContainer = UniGreenDark

val DarkSecondary = UniGreenLight
val DarkOnSecondary = UniGreenDark

val DarkTertiary = UniGreenLight
val DarkOnTertiary = UniGreenDark
val DarkTertiaryContainer = UniGreenLight.copy(alpha = 0.2f)
val DarkOnTertiaryContainer = UniGreenDark

val DarkBackGround = UniNeutralDark
val DarkSurface = UniNeutralDark
val DarkOnSurface = Color.White
val DarkSurfaceVariant = UniGreenLight.copy(alpha = 0.2f)
val DarkOnSurfaceVariant = UniGreenDark

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color.White

/**
 * B. COLORES DE ESTADO (para las Tarjetas específicas):
 */
// Rojo Suave para la tarjeta "Ausente"
val StatusAbsentRed = Color(0xFFFFEBEE) // Rojo muy claro de fondo
val StatusAbsentRedText = Color(0xFFD32F2F) // Rojo oscuro para texto/borde

// Verde Suave para la tarjeta "Presente"
val StatusPresentGreen = Color(0xFFE8F5E9) // Verde muy claro de fondo
val StatusPresentGreenText = Color(0xFF388E3C) // Verde oscuro para texto/borde

// Amarillo Suave para la tarjeta "Tarde"
val StatusWarningYellow = Color(0xFFFFFDE7) // Amarillo muy claro de fondo
val StatusWarningYellowText = Color(0xFFFBC02D) // Amarillo oscuro para texto/borde