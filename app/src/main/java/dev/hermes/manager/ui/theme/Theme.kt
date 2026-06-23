package dev.hermes.manager.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = HermesOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5C2E1E),
    onPrimaryContainer = HermesOrangeLight,
    secondary = HermesOnSurfaceMuted,
    background = HermesBg,
    surface = HermesSurface,
    surfaceVariant = HermesSurfaceVariant,
    onBackground = HermesOnSurface,
    onSurface = HermesOnSurface,
    onSurfaceVariant = HermesOnSurfaceMuted,
    outline = HermesDivider,
    error = HermesError
)

@Composable
fun HermesManagerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = HermesTypography,
        content = content
    )
}
