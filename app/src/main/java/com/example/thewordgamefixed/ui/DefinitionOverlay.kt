package com.example.thewordgamefixed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.thewordgamefixed.logic.DictionaryManager

@Composable
fun DefinitionOverlay(
    word: String,
    onClose: () -> Unit
) {
    val definition = DictionaryManager.getDefinition(word) ?: "Определение не найдено."

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
                .fillMaxHeight(0.6f)
                .background(Color.White.copy(alpha = 0.95f), shape = MaterialTheme.shapes.medium)
                .padding(20.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onClose) {
                        Text("✖", style = MaterialTheme.typography.headlineSmall)
                    }
                }

                Text(
                    text = word.uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                androidx.compose.foundation.rememberScrollState().let { scroll ->
                    Column(modifier = Modifier.verticalScroll(scroll)) {
                        Text(
                            text = definition,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}