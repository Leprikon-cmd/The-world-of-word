package com.example.thewordgamefixed.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CrosswordView(words: List<String>) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        words.forEach { word ->
            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                word.forEach { letter ->
                    Text(
                        text = letter.toString(),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}