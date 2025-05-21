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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 160.dp)
                .padding(bottom = 16.dp)
        ) {
            items(validWords) { word ->
                Text(
                    text = word,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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