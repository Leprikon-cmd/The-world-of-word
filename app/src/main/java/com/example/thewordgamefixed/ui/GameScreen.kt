package com.example.thewordgamefixed.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thewordgamefixed.logic.GameLogic
import com.example.thewordgamefixed.viewmodel.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val validWords = GameLogic.getValidWords().sorted() // ✅ Слова, допустимые для текущей звезды

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // ➕ отступ от краёв
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔠 Отображаем список слов (для отладки)
        val words = viewModel.validWords
            .sortedWith(compareByDescending<String> { it.length }.thenBy { it })

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // ➕ по 2 слова в строке
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 220.dp) // ограничиваем высоту
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(validWords) { word ->
                Text(
                    text = word,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔘 Кнопка "Новый раунд"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                GameLogic.generateNewLevel()
                viewModel.clearSelection()
                viewModel.updateWords()
            }) {
                Text("🔄 Новый раунд")
            }
        }

        // 🌟 Игровая звезда по центру
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // чтобы заняла оставшееся пространство
            contentAlignment = Alignment.Center
        ) {
            GameBoard(viewModel)
        }
    }
}