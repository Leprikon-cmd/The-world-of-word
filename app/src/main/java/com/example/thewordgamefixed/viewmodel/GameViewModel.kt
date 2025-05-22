package com.example.thewordgamefixed.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.thewordgamefixed.logic.GameLogic


class GameViewModel : ViewModel() {
    val selectedLetters = mutableStateListOf<Char>()
    val result = mutableStateOf("")
    val validWords = mutableStateListOf<String>()

    private var lastAddedChar: Char? = null // 🧠 защита от многократного тапа по той же букве подряд

    fun validateWord() {
        val word = selectedLetters.joinToString("")
        result.value = if (GameLogic.isValidWord(word)) {
            "✅ Верно!"
        } else {
            "❌ Неверно"
        }
    }

    fun updateWords() {
        validWords.clear()
        validWords.addAll(GameLogic.getValidWords().sortedByDescending { it.length })
    }

    fun addLetter(letter: Char) {
        selectedLetters.add(letter)
    }

    fun clearSelection() {
        selectedLetters.clear()
        result.value = ""
        lastAddedChar = null // сбрасываем для нового слова
    }

    fun getWord(): String = selectedLetters.joinToString("")
}