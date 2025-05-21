// Оформление внешнего вида: цвета, шрифты, стили
// Сейчас можно не трогать — подключим позже

package com.example.thewordgamefixed.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF0061A4),
    secondary = androidx.compose.ui.graphics.Color(0xFF4E8098),
)

@Composable
fun TheWordGameFixedTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}