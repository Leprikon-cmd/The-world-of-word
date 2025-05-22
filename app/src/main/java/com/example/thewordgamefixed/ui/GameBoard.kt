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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape

@Composable
fun GameBoard(viewModel: GameViewModel = viewModel()) {
    val letters = GameLogic.getLetters()
    val starPoints = remember { mutableStateListOf<Pair<Offset, Char>>() }
    val density = LocalDensity.current

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    // --- ✅ НАСТРОЙКИ ВИЗУАЛА ---
    val starSize = Visual.starSize
    val radiusDp = Visual.starRadius
    val letterCircleSize = Visual.letterCircleSize
    val letterFontSize = Visual.letterFontSize
    val touchRadius = Visual.swipeTouchRadius
    var lastIndex by remember { mutableStateOf<Int?>(null) }
    var lastFrameMiss = false

    val radiusPx = with(density) { radiusDp.toPx() }
    val letterRadiusPx = with(density) { touchRadius.toPx() }

    // 🔁 Храним историю координат, по которым уже свайпали в рамках одного слова
    val usedIndices = remember { mutableStateListOf<Int>() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 8.dp)
                .background(Color.White.copy(alpha = 0.6f), shape = CircleShape)
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = viewModel.getWord(),
                fontSize = 24.sp,
                color = Color.Black // чтобы чётко читалось
            )
        }

        Text(
            text = viewModel.result.value,
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        Box(
            modifier = Modifier
                .size(starSize)
                .onGloballyPositioned { coordinates ->
                    boxSize = coordinates.size
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            viewModel.clearSelection()
                            checkTouched(offset, starPoints, letterRadiusPx)?.let { index ->
                                val (_, char) = starPoints[index]
                                viewModel.addLetter(char)
                                lastIndex = index
                                lastFrameMiss = false
                            }
                        },
                        onDrag = { change, _ ->
                            checkTouched(change.position, starPoints, letterRadiusPx)?.let { index ->
                                if (index != lastIndex || lastFrameMiss) {
                                    val (_, char) = starPoints[index]
                                    viewModel.addLetter(char)
                                    lastIndex = index
                                    lastFrameMiss = false
                                }
                            } ?: run {
                                lastFrameMiss = true
                            }
                        },
                        onDragEnd = {
                            val word = viewModel.getWord()
                            viewModel.tryAddWord(word)
                            viewModel.clearSelection()
                            lastIndex = null
                            lastFrameMiss = false
                        }
                    )
                }
        ) {
            if (boxSize != IntSize.Zero) {
                val center = Offset(boxSize.width / 2f, boxSize.height / 2f)

                starPoints.clear()
                letters.forEachIndexed { index, letter ->
                    val angleDeg = -90 + 144 * index
                    val angleRad = angleDeg * (PI / 180f)
                    val x = center.x + cos(angleRad) * radiusPx
                    val y = center.y + sin(angleRad) * radiusPx
                    starPoints.add(Offset(x.toFloat(), y.toFloat()) to letter)

                    Box(
                        modifier = Modifier
                            .offset {
                                val offset = with(density) { (letterCircleSize / 2).toPx() }
                                IntOffset((x - offset).toInt(), (y - offset).toInt())
                            }
                            .size(letterCircleSize) // размер круга
                            .background(
                                color = Color.White.copy(alpha = 0.6f), // 🔘 белый полупрозрачный
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(letter.toString(), fontSize = letterFontSize)
                    }
                }
            }
        }
    }
}

// ✅ Возвращает индекс буквы, по которой попали при свайпе
fun checkTouched(pos: Offset, points: List<Pair<Offset, Char>>, radius: Float): Int? {
    for ((index, pair) in points.withIndex()) {
        val (offset, _) = pair
        if ((offset - pos).getDistance() <= radius) return index
    }
    return null
}