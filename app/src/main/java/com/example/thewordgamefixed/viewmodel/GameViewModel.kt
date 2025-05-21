// ViewModel: хранит текущее слово и результат (верно/неверно)
// Связывает GameBoard и GameScreen — единый источник правды

package com.example.thewordgamefixed.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.thewordgamefixed.logic.GameLogic

class GameViewModel : ViewModel() {
    val selectedLetters = mutableStateListOf<Char>()
    val result = mutableStateOf("")

    fun validateWord() {
        val word = selectedLetters.joinToString("")
        result.value = if (GameLogic.isValidWord(word)) {
            "✅ Верно!"
        } else {
            "❌ Неверно"
        }
    }

    fun addLetter(letter: Char) {
        if (!selectedLetters.contains(letter)) {
            selectedLetters.add(letter)
        }
    }

    fun clearSelection() {
        selectedLetters.clear()
        result.value = "" // сброс результата при начале нового слова
    }

    fun getWord(): String = selectedLetters.joinToString("")
}