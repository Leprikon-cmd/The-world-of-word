// Отображение кроссворда на основе результата CrosswordBuilder
// Использует фиксированную сетку 15x15

// Отрисовка кроссворда по сетке
package com.example.thewordgamefixed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thewordgamefixed.logic.CrosswordBuilder

@Composable
fun CrosswordView(crossword: CrosswordBuilder.CrosswordResult) {
    val cellSize = 28.dp // чуть больше

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(bottom = 16.dp), // отступ вниз
        contentAlignment = Alignment.Center
    ) {
        Column {
            crossword.grid.forEach { row ->
                Row {
                    row.forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .padding(1.dp), // 🔧 добавлен отступ между ячейками
                            contentAlignment = Alignment.Center
                        ) {
                            if (c != ' ') {
                                Text(text = c.toString(), fontSize = 20.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}