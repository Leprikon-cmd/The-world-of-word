// Визуальные параметры игры: размеры, шрифты, радиусы
package com.example.thewordgamefixed.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object Visual {
    val starSize: Dp = 220.dp             // размер области под звезду
    val starRadius: Dp = 100.dp            // радиус размещения букв
    val letterCircleSize: Dp = 40.dp      // размер ячеек с буквами
    val letterFontSize: TextUnit = 32.sp  // шрифт букв
    val wordFontSize: TextUnit = 24.sp    // шрифт набранного слова
    val resultFontSize: TextUnit = 18.sp  // шрифт результата валидации
    val swipeTouchRadius: Dp = 32.dp      // радиус определения свайпа
    val paddingBetweenBlocks: Dp = 5.dp  // отступ между кроссвордом и звездой
}