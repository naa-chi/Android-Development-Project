package com.example.juntavecinos.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class CustomColors(
    val greenText: Color,
    val redText : Color,
    val availableStatus: Color,
    val fullStatus: Color,
    val gray1 : Color,
    val gray2 : Color,
    val gray3 : Color
)

val LightCustomColors = CustomColors(
    greenText = Color(0xFF2E7D32),
    redText = Color(0xFF5B0F0F),
    availableStatus = Color(0xFF0072B2),
    fullStatus = Color(0xFFD55E00),
    gray1 = Color(0xFFA3A7C7),
    gray2 = Color(0xFF8792A4),
    gray3 = Color(0xFF383941),
)

val DarkCustomColors = CustomColors(
    greenText = Color(0xFF8FC291),
    redText = Color(0xFFFF9AB6),
    availableStatus = Color(0xFF56B4E9),
    fullStatus = Color(0xFFE69F00),
    gray1 = Color(0xFFA3A7C7),
    gray2 = Color(0xFF8792A4),
    gray3 = Color(0xFF383941),
)

//Add custom colors
val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }
val MaterialTheme.customColors: CustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColors.current

private val DarkColorScheme = darkColorScheme(
    primary = Blue4,
    secondary = Blue5,
    tertiary = Blue6,
    background = Blue6,
    onBackground = Blue1,
    onPrimary = Blue1,
    onSecondary = Blue1,
    onTertiary = Blue1
)

private val LightColorScheme = lightColorScheme(
    primary = Blue3,
    secondary = Blue2,
    tertiary = Blue1,
    background = Blue1,
    onBackground = Blue4,
    onPrimary = Blue1,
    onSecondary = Blue1,
    onTertiary = Blue5
)

@Composable
fun JuntaVecinosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Select the custom color palette matching current theme mode
    val customColors = if (darkTheme) DarkCustomColors else LightCustomColors

    // 5. Provide custom colors to the hierarchy
    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}