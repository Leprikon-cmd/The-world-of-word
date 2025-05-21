// Отображение звезды из букв
// Отслеживает свайп от буквы к букве и собирает слово

package com.example.thewordgamefixed.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thewordgamefixed.logic.GameLogic
import com.example.thewordgamefixed.viewmodel.GameViewModel
import kotlin.math.*

@Composable
fun GameBoard(viewModel: GameViewModel = viewModel()) {
    val letters = GameLogic.getLetters()
    val starPoints = remember { mutableStateListOf<Pair<Offset, Char>>() }
    val density = LocalDensity.current

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    // --- ✅ НАСТРОЙКИ ВИЗУАЛА ---
    val starSize = 220.dp             // ⬅️ общий размер области звезды
    val radiusDp = 80.dp              // ⬅️ радиус звезды
    val letterCircleSize = 32.dp      // ⬅️ размер буквы
    val letterFontSize = 20.sp        // ⬅️ размер шрифта
    val touchRadius = 30.dp           // ⬅️ радиус для свайпа
    // ---------------------------

    val radiusPx = with(density) { radiusDp.toPx() }
    val letterRadiusPx = with(density) { touchRadius.toPx() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally // ➕ Центруем всё по горизонтали
    ) {
        // 👉 Отображение текущего набранного слова
        Text(
            text = viewModel.getWord(),
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
        )

        // 👉 Отображение результата проверки ("✅ Верно!" / "❌ Неверно")
        Text(
            text = viewModel.result.value,
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        // 👉 Контейнер для звезды из букв
        Box(
            modifier = Modifier
                .size(starSize) // 📐 общий размер области звезды
                .onGloballyPositioned { coordinates ->
                    boxSize = coordinates.size // сохраняем размер Box в пикселях
                }
                .pointerInput(Unit) { // 🔁 Отслеживаем свайпы по звезде
                    detectDragGestures(
                        onDragStart = { offset ->
                            viewModel.clearSelection() // сбрасываем набранное
                            checkTouched(offset, starPoints, letterRadiusPx)?.let {
                                viewModel.addLetter(it) // добавляем букву по нажатию
                            }
                        },
                        onDrag = { change, _ ->
                            checkTouched(change.position, starPoints, letterRadiusPx)?.let {
                                viewModel.addLetter(it) // добавляем букву по свайпу
                            }
                        },
                        onDragEnd = {
                            viewModel.validateWord() // проверка, когда пользователь отпустил палец
                        }
                    )
                }
        ) {
            if (boxSize != IntSize.Zero) {
                val center = Offset(boxSize.width / 2f, boxSize.height / 2f) // 🎯 центр звезды

                starPoints.clear() // очищаем старые координаты

                // 🔁 Расставляем 5 букв по кругу в форме пятиконечной звезды
                letters.forEachIndexed { index, letter ->
                    val angleDeg = -90 + 144 * index // угол для 5-конечной звезды
                    val angleRad = angleDeg * (PI / 180f) // в радианах
                    val x = center.x + cos(angleRad) * radiusPx
                    val y = center.y + sin(angleRad) * radiusPx
                    starPoints.add(Offset(x.toFloat(), y.toFloat()) to letter)

                    // 📦 Отображаем одну из букв звезды
                    Box(
                        modifier = Modifier
                            .offset {
                                // Центрируем ячейку по координатам
                                val offset = with(density) { (letterCircleSize / 2).toPx() }
                                IntOffset((x - offset).toInt(), (y - offset).toInt())
                            }
                            .size(letterCircleSize), // размер ячейки с буквой
                        contentAlignment = Alignment.Center
                    ) {
                        Text(letter.toString(), fontSize = letterFontSize)
                    }
                }
            }
        }
    }
}

// ✅ Проверка, попал ли пользователь в какую-то букву при свайпе
fun checkTouched(pos: Offset, points: List<Pair<Offset, Char>>, radius: Float): Char? {
    for ((offset, char) in points) {
        if ((offset - pos).getDistance() <= radius) return char
    }
    return null
}