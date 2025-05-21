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
import com.example.thewordgamefixed.logic.GameLogic // ✅ путь исправлен
import com.example.thewordgamefixed.viewmodel.GameViewModel
import kotlin.math.*

@Composable
fun GameBoard(viewModel: GameViewModel = viewModel()) {
    val letters = GameLogic.getLetters()
    val starPoints = remember { mutableStateListOf<Pair<Offset, Char>>() }
    val density = LocalDensity.current

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    val radiusPx = with(density) { 90.dp.toPx() }
    val letterRadius = with(density) { 30.dp.toPx() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = viewModel.getWord(),
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = viewModel.result.value,
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .size(250.dp)
                .onGloballyPositioned { coordinates ->
                    boxSize = coordinates.size
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            viewModel.clearSelection()
                            checkTouched(offset, starPoints, letterRadius)?.let {
                                viewModel.addLetter(it)
                            }
                        },
                        onDrag = { change, _ ->
                            checkTouched(change.position, starPoints, letterRadius)?.let {
                                viewModel.addLetter(it)
                            }
                        },
                        onDragEnd = {
                            viewModel.validateWord()
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
                                val offset = with(density) { 12.dp.toPx() }
                                IntOffset((x - offset).toInt(), (y - offset).toInt())
                            }
                            .size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(letter.toString(), fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

fun checkTouched(pos: Offset, points: List<Pair<Offset, Char>>, radius: Float): Char? {
    for ((offset, char) in points) {
        if ((offset - pos).getDistance() <= radius) return char
    }
    return null
}