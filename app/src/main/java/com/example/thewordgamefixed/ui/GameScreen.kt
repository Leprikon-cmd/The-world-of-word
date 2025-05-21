// Главный экран игры: отображает кроссворд и звезду

package com.example.thewordgamefixed.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thewordgamefixed.logic.GameLogic
import com.example.thewordgamefixed.logic.CrosswordBuilder
import com.example.thewordgamefixed.viewmodel.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val words = GameLogic.getCurrentWords().toList()
    val crossword = CrosswordBuilder.buildCrossword(words)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // ✅ Отображаем кроссворд, если удалось построить
        crossword?.let {
            CrosswordView(it)
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ✅ Отображаем игровую звезду
        GameBoard(viewModel)
    }
}