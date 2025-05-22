package com.example.thewordgamefixed.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thewordgamefixed.logic.GameLogic
import com.example.thewordgamefixed.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    // ✅ Загружаем текущий фон из assets
    val context = LocalContext.current
    val imageBitmap = remember(viewModel.backgroundImage.value) {
        val assetManager = context.assets
        val inputStream = assetManager.open("Background/${viewModel.backgroundImage.value}")
        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
    }

    // ✅ Все допустимые слова, отсортированные по длине и алфавиту
    val words = viewModel.validWords
        .sortedWith(compareByDescending<String> { it.length }.thenBy { it })

    val showWordListForDebug = true // ← Меняй на true для отладки
    val foundWords = viewModel.foundWords

    Box(modifier = Modifier.fillMaxSize()) {
        // 📷 Фоновое изображение
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 🧩 Основной контент поверх фона
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Найдено: ${viewModel.getFoundWordCount()} из ${viewModel.getTotalValidWordCount()}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Очки: ${viewModel.score.value}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 📝 Отображаем список слов — максимум полэкрана
            if (showWordListForDebug) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 220.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(viewModel.validWords) { word ->
                        val isFound = viewModel.foundWords.contains(word)

                        Text(
                            text = if (isFound) word else "-".repeat(word.length),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // 🔘 Кнопка перезапуска
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    GameLogic.generateNewLevel()
                    viewModel.updateWords()         // 🔄 сначала обновляем список слов
                    viewModel.resetGame()           // 🧹 очищаем всё старое (включая foundWords и score)
                    viewModel.pickNewBackground()   // 🎨 новый фон
                }) {
                    Text("🔄 Новый раунд")
                }
            }

            // ⭐ Звезда — занимает оставшееся место
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                GameBoard(viewModel)
            }
        }
    }
}