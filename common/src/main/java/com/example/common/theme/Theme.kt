package com.example.common.theme

import Typography
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkTeal = Color(0xFF0f262d)
private val PaleBlue = Color(0xFFF4F9FB)
private val DarkGreen = Color(0xFF050F0C)
private val DarkBlueGrey = Color(0xFF1c2a33)
private val BrightRed = Color(0xFFD32F2F)
private val DeepRed = Color(0xFFC62828)
private val White = Color.White

private val LightColorScheme = lightColorScheme(
    primary = DarkTeal,
    secondary = PaleBlue,
    tertiary = DarkGreen,
    background = White,
    surface = PaleBlue,
    onPrimary = White,
    onSecondary = DarkTeal,
    onBackground = DarkTeal,
    onSurface = DarkTeal,
    error = DeepRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkTeal,
    secondary = PaleBlue,
    tertiary = DarkGreen,
    background = DarkTeal,
    surface = DarkBlueGrey,
    onPrimary = White,
    onSecondary = DarkTeal,
    onBackground = White,
    onSurface = PaleBlue,
    error = BrightRed,
    onError = White

)

@Composable
fun TestAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}