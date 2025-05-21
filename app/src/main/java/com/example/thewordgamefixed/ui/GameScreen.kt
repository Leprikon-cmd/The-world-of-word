package com.example.thewordgamefixed.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween, // ⬅️ Разделим верх/низ
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // ✅ Отображаем кроссворд, если удалось построить
            crossword?.let {
                CrosswordView(it)
                Spacer(modifier = Modifier.height(Visual.paddingBetweenBlocks))
            }
        }

        // ✅ Отображаем игровую звезду внизу, но с запасом
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 26.dp), // ➕ Отступ от нижнего края
            contentAlignment = Alignment.Center
        ) {
            GameBoard(viewModel)
        }
    }
}