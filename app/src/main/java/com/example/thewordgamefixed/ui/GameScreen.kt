package com.example.thewordgamefixed.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thewordgamefixed.logic.GameLogic
import com.example.thewordgamefixed.viewmodel.GameViewModel
import androidx.compose.foundation.shape.CircleShape

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val context = LocalContext.current
    val imageBitmap = remember(viewModel.backgroundImage.value) {
        val assetManager = context.assets
        val inputStream = assetManager.open("Background/${viewModel.backgroundImage.value}")
        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
    }

    LaunchedEffect(Unit) {
        viewModel.updateWords()
    }

    var showWordList by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (viewModel.showWinDialog.value) {
            AlertDialog(
                onDismissRequest = { viewModel.showWinDialog.value = false },
                confirmButton = {
                    TextButton(onClick = { viewModel.showWinDialog.value = false }) {
                        Text("ОК")
                    }
                },
                title = { Text("🎉 Победа!") },
                text = { Text("Вы нашли все слова на этом уровне!") }
            )
        }

        val symbol = viewModel.lastResultSymbol.value

        symbol?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it,
                    fontSize = 48.sp,
                    color = if (it == "✅") Color.Green else Color.Red
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔝 Верхняя панель
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 📜 Кнопка списка слов
                IconButton(
                    onClick = { showWordList = !showWordList },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.4f), CircleShape)
                ) {
                    Text("📜")
                }

                // ⬆️ Центр: Найдено + Очки (в столбик)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.4f), shape = CircleShape)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Найдено: ${viewModel.getFoundWordCount()} из ${viewModel.getTotalValidWordCount()}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp)) // небольшой отступ

                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.4f), shape = CircleShape)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Очки: ${viewModel.score.value}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // 🔄 Кнопка перезапуска
                IconButton(
                    onClick = {
                        GameLogic.generateNewLevel()
                        viewModel.updateWords()
                        viewModel.resetGame()
                        viewModel.pickNewBackground()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.4f), CircleShape)
                ) {
                    Text("🔄")
                }
            }

            // ⭐ Игровая звезда
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                GameBoard(viewModel)
            }
        }

        // 📜 Оверлей со списком слов
        if (showWordList) {
            WordListOverlay(viewModel = viewModel) {
                showWordList = false
            }
        }
    }
}