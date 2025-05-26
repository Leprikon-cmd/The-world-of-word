package com.example.thewordgamefixed.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.thewordgamefixed.viewmodel.GameViewModel
import com.example.thewordgamefixed.logic.DictionaryManager
import java.io.IOException

@Composable
fun WordListOverlay(
    viewModel: GameViewModel,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val assetManager = context.assets
    val background = remember {
        try {
            val input = assetManager.open("Background/123.png")
            android.graphics.BitmapFactory.decodeStream(input).asImageBitmap()
        } catch (e: IOException) {
            null
        }
    }

    val words = viewModel.validWords.sortedWith(compareByDescending<String> { it.length }.thenBy { it })
    val found = viewModel.foundWords

    var selectedWord by remember { mutableStateOf<String?>(null) }

    // 🎯 Если слово выбрано, показываем определение
    if (selectedWord != null) {
        DefinitionOverlay(
            word = selectedWord!!,
            onClose = { selectedWord = null }
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.75f)
        ) {
            background?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = { onClose() }) {
                        Text("✖", style = MaterialTheme.typography.headlineSmall)
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 80.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(words) { word ->
                        val isFound = word in found
                        val display = if (isFound) word else "✨".repeat(word.length)

                        Text(
                            text = display,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .clickable(enabled = isFound) {
                                    selectedWord = word
                                }
                        )
                    }
                }
            }
        }
    }
}