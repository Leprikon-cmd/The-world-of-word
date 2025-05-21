// Экран игры: включает кроссворд и звезду

package com.example.thewordgamefixed.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thewordgamefixed.logic.GameLogic
import com.example.thewordgamefixed.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Кроссворд (5 слов сверху)
        CrosswordView(GameLogic.getCurrentWords())

        Spacer(modifier = Modifier.height(24.dp))

        // Звезда из букв
        GameBoard(viewModel)
    }
}