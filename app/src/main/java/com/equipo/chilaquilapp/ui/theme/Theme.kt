package com.equipo.chilaquilapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta "Katania Festive Cantina" mapeada al esquema de color de Material3,
// generada a partir de los tokens de los mockups (sección Color en DESIGN.md).
private val LightColorScheme = lightColorScheme(
    primary = DeepOlive,
    onPrimary = Color.White,
    primaryContainer = Mustard,
    onPrimaryContainer = DeepOliveVariant,
    inversePrimary = MustardDim,
    secondary = SpicedOrange,
    onSecondary = Color.White,
    secondaryContainer = SpicedOrangeContainer,
    onSecondaryContainer = BrownContainerOn,
    tertiary = EarthGrey,
    onTertiary = Color.White,
    tertiaryContainer = EarthGreyContainer,
    onTertiaryContainer = EarthGreyContainerOn,
    background = SurfaceLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceTint = DeepOlive,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    outline = Outline,
    outlineVariant = OutlineVariant,
    surfaceDim = SurfaceDim,
    surfaceBright = SurfaceLight,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest
)

// Tema oscuro derivado de los tokens "fixed"/"inverse" del mismo sistema de
// diseño, para que la app no caiga en el morado por defecto de Material3.
private val DarkColorScheme = darkColorScheme(
    primary = MustardDim,
    onPrimary = OliveDarkest,
    primaryContainer = OliveDark,
    onPrimaryContainer = MustardFixed,
    inversePrimary = DeepOlive,
    secondary = SpicedOrangeFixedDim,
    onSecondary = BrownDarkest,
    secondaryContainer = BrownDark,
    onSecondaryContainer = SpicedOrangeFixed,
    tertiary = EarthGreyFixedDim,
    onTertiary = EarthGreyDarkest,
    tertiaryContainer = EarthGreyDark,
    onTertiaryContainer = EarthGreyFixed,
    background = DarkSurface,
    onBackground = InverseOnSurface,
    surface = DarkSurface,
    onSurface = InverseOnSurface,
    surfaceVariant = EarthGreyDark,
    onSurfaceVariant = OutlineVariant,
    surfaceTint = MustardDim,
    inverseSurface = SurfaceLight,
    inverseOnSurface = OnSurfaceLight,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = OnErrorContainerLight,
    onErrorContainer = ErrorContainerLight,
    outline = DarkOutline,
    outlineVariant = EarthGreyDark,
    surfaceDim = DarkSurfaceDim,
    surfaceBright = DarkSurfaceBright,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest
)

@Composable
fun ChilaquilAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
