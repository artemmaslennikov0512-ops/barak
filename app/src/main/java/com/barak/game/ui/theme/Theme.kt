package com.barak.game.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Beige = Color(0xFFDDD8D0)
val Panel = Color(0xFFEFEBE4)
val Ink = Color(0xFF2C2925)
val Muted = Color(0xFF6A645C)
val Accent = Color(0xFFA85A32)
val Danger = Color(0xFF8B3A2F)
val Ok = Color(0xFF5F7A4A)
val Border = Color(0xFFC2BBB0)

private val colors = lightColorScheme(
    primary = Accent,
    onPrimary = Color.White,
    background = Beige,
    onBackground = Ink,
    surface = Panel,
    onSurface = Ink,
    secondary = Muted,
    error = Danger,
)

@Composable
fun BarakTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography.copy(
            headlineLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                color = Ink,
            ),
            titleLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = Ink,
            ),
            bodyLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                color = Ink,
            ),
            bodyMedium = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                color = Muted,
            ),
        ),
        content = content,
    )
}
