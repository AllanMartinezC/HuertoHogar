package com.example.huerto_hogar.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores personalizados
val VerdePrincipal = Color(0xFF4CAF50)
val VerdeClaro = Color(0xFF81C784)
val Amarillo = Color(0xFFFFEB3B)
val Naranja = Color(0xFFFF9800)
val Blanco = Color(0xFFFFFFFF)
val GrisFondo = Color(0xFFF0F4F0)
val Marron = Color(0xFF795548)

private val HuertoHogarColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    onPrimary = Blanco,
    secondary = VerdeClaro,
    onSecondary = Color.Black,
    background = Blanco,
    onBackground = Color.Black,
    surface = Blanco,
    onSurface = Color.Black,
    error = Color.Red,
    onError = Blanco
)

@Composable
fun HuertoHogarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HuertoHogarColorScheme,
        typography = Typography(),
        content = content
    )
}
