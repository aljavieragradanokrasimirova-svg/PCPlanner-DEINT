package com.example.pcplanner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF2456A6), onPrimary = Color.White,
    secondary = Color(0xFF386A60), background = Color(0xFFF5F7FB),
    surface = Color(0xFFFFFFFF), surfaceVariant = Color(0xFFE7EDF7)
)
private val DarkColors = darkColorScheme(
    primary = Color(0xFFA9C7FF), onPrimary = Color(0xFF002F67),
    secondary = Color(0xFFA0D0C2), background = Color(0xFF10141C),
    surface = Color(0xFF191E28), surfaceVariant = Color(0xFF293344)
)

/** Aplica los colores Material 3 del planificador.
 * @param darkTheme Permite comprobar el diseño en modo claro y oscuro.
 * @param content Contenido visual al que se aplica el tema.
 */
@Composable
fun PlannerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, content = content)
}
