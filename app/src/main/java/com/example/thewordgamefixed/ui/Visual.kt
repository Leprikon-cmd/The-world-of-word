// Визуальные параметры игры: размеры, шрифты, радиусы
package com.example.thewordgamefixed.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object Visual {
    val starSize: Dp = 220.dp             // размер области под звезду
    val starRadius: Dp = 80.dp            // радиус размещения букв
    val letterCircleSize: Dp = 32.dp      // размер ячеек с буквами
    val letterFontSize: TextUnit = 20.sp  // шрифт букв
    val wordFontSize: TextUnit = 24.sp    // шрифт набранного слова
    val resultFontSize: TextUnit = 18.sp  // шрифт результата валидации
    val swipeTouchRadius: Dp = 30.dp      // радиус определения свайпа
    val paddingBetweenBlocks: Dp = 16.dp  // отступ между кроссвордом и звездой
}